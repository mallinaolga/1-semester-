package com.mipt.olgamallina.dto;

import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "DTO for creating a new task")
public class TaskCreateDto {

    @NotBlank(groups = OnCreate.class)
    @Size(min = 3, max = 100, groups = OnCreate.class)
    @Schema(description = "Task title", example = "Finish homework")
    private String title;

    @Size(max = 500, groups = OnCreate.class)
    @Schema(description = "Task description", example = "Complete Spring Boot homework")
    private String description;

    @NotNull(groups = OnCreate.class)
    @FutureOrPresent(groups = OnCreate.class)
    @Schema(description = "Due date", example = "2026-03-30")
    private LocalDate dueDate;

    @NotNull(groups = OnCreate.class)
    @Schema(description = "Task priority", example = "HIGH")
    private Priority priority;

    @Size(max = 5, groups = OnCreate.class)
    @Schema(description = "Task tags")
    private Set<String> tags = new HashSet<>();

    public TaskCreateDto() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags != null ? tags : new HashSet<>();
    }
}