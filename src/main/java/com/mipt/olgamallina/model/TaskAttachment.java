package com.mipt.olgamallina.model;

import java.time.LocalDateTime;

public class TaskAttachment {

    private Long id;
    private Long taskId;
    private String fileName;
    private String storedFileName;
    private String contentType;
    private long size;
    private LocalDateTime uploadedAt;

    public TaskAttachment() {
    }

    public TaskAttachment(Long id, Long taskId, String fileName, String storedFileName,
                          String contentType, long size, LocalDateTime uploadedAt) {
        this.id = id;
        this.taskId = taskId;
        this.fileName = fileName;
        this.storedFileName = storedFileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}