package com.mipt.olgamallina.service;

import com.mipt.olgamallina.exception.AttachmentNotFoundException;
import com.mipt.olgamallina.exception.FileStorageException;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.model.TaskAttachment;
import com.mipt.olgamallina.repository.TaskAttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {

    private final TaskAttachmentRepository attachmentRepository;
    private final TaskService taskService;
    private final Path uploadPath;

    public AttachmentService(TaskAttachmentRepository attachmentRepository,
                             TaskService taskService,
                             @Value("${app.upload-dir:uploads}") String uploadDir) {
        this.attachmentRepository = attachmentRepository;
        this.taskService = taskService;
        this.uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadPath);
        } catch (Exception e) {
            throw new FileStorageException("Could not create upload directory", e);
        }
    }

    public TaskAttachment storeAttachment(Long taskId, MultipartFile file) {
        taskService.getById(taskId);

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Uploaded file is empty");
        }

        String originalFilename = file.getOriginalFilename() != null
                ? file.getOriginalFilename()
                : "unknown.file";

        String storedFileName = UUID.randomUUID() + "_" + originalFilename;
        Path targetPath = uploadPath.resolve(storedFileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath);

            TaskAttachment attachment = new TaskAttachment();
            attachment.setTaskId(taskId);
            attachment.setFileName(originalFilename);
            attachment.setStoredFileName(storedFileName);
            attachment.setContentType(file.getContentType());
            attachment.setSize(file.getSize());
            attachment.setUploadedAt(LocalDateTime.now());

            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    public TaskAttachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
    }

    public Resource loadAsResource(Long attachmentId) {
        TaskAttachment attachment = getAttachment(attachmentId);
        Path path = uploadPath.resolve(attachment.getStoredFileName()).normalize();
        Resource resource = new FileSystemResource(path);

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileStorageException("File not found on disk");
        }

        return resource;
    }

    public void deleteAttachment(Long attachmentId) {
        TaskAttachment attachment = getAttachment(attachmentId);
        Path path = uploadPath.resolve(attachment.getStoredFileName()).normalize();

        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            throw new FileStorageException("Failed to delete file from disk", e);
        }

        attachmentRepository.deleteById(attachmentId);
    }

    public List<TaskAttachment> getTaskAttachments(Long taskId) {
        taskService.getById(taskId);
        return attachmentRepository.findByTaskId(taskId);
    }
}