package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.dto.AttachmentResponseDto;
import com.mipt.olgamallina.model.TaskAttachment;
import com.mipt.olgamallina.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @Operation(summary = "Upload attachment for task")
    @ApiResponse(responseCode = "201", description = "Attachment uploaded")
    @PostMapping(value = "/api/tasks/{taskId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDto> upload(
            @PathVariable Long taskId,
            @RequestPart("file") MultipartFile file) {

        TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
        AttachmentResponseDto dto = new AttachmentResponseDto(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getSize(),
                attachment.getUploadedAt()
        );

        return ResponseEntity.status(201).body(dto);
    }

    @Operation(summary = "Download attachment by id")
    @ApiResponse(responseCode = "200", description = "File downloaded")
    @GetMapping("/api/attachments/{attachmentId}")
    public ResponseEntity<Resource> download(@PathVariable Long attachmentId) {
        TaskAttachment attachment = attachmentService.getAttachment(attachmentId);
        Resource resource = attachmentService.loadAsResource(attachmentId);

        String contentType = attachment.getContentType() != null
                ? attachment.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Operation(summary = "Delete attachment by id")
    @ApiResponse(responseCode = "204", description = "Attachment deleted")
    @DeleteMapping("/api/attachments/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all attachments for task")
    @ApiResponse(responseCode = "200", description = "Attachment list returned")
    @GetMapping("/api/tasks/{taskId}/attachments")
    public ResponseEntity<List<AttachmentResponseDto>> getTaskAttachments(@PathVariable Long taskId) {
        List<AttachmentResponseDto> response = attachmentService.getTaskAttachments(taskId).stream()
                .map(a -> new AttachmentResponseDto(a.getId(), a.getFileName(), a.getSize(), a.getUploadedAt()))
                .toList();

        return ResponseEntity.ok(response);
    }
}