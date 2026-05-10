package com.mipt.olgamallina.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentResponseDto {
    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;
    private Long taskId;
}