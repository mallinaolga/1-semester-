package com.mipt.olgamallina.dto;

import com.mipt.olgamallina.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TaskCreateDto {
    @NotBlank
    private String title;
    private String description;

    @NotNull
    private Priority priority;

    private LocalDateTime dueDate;
    private Set<String> tags;
}