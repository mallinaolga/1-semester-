package com.mipt.olgamallina.dto;

import com.mipt.olgamallina.model.Priority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TaskUpdateDto {
    private String title;
    private String description;
    private Boolean completed;
    private Priority priority;
    private LocalDateTime dueDate;
    private Set<String> tags;
}