package com.mipt.olgamallina.dto;

import com.mipt.olgamallina.model.Priority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Priority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> tags;
}