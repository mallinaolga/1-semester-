package com.mipt.olgamallina.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.validation.DueDateNotBeforeCreation;
import com.mipt.olgamallina.validation.OnUpdate;
import com.mipt.olgamallina.validation.TaskDueDateCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@DueDateNotBeforeCreation(groups = OnUpdate.class)
@Schema(description = "DTO for updating an existing task")
public class TaskUpdateDto implements TaskDueDateCheck {

    @Size(min = 3, max = 100, groups = OnUpdate.class)
    @Schema(description = "Task title", example = "Updated title")
    private String title;

    @Size(max = 500, groups = OnUpdate.class)
    @Schema(description = "Task description")
    private String description;

    @Schema(description = "Completion status")
    private Boolean completed;

    @FutureOrPresent(groups = OnUpdate.class)
    @Schema(description = "Due date")
    private LocalDate dueDate;

    @Schema(description = "Task priority")
    private Priority priority;

    @Size(max = 5, groups = OnUpdate.class)
    @Schema(description = "Task tags")
    private Set<String> tags = new HashSet<>();

    @JsonIgnore
    @Schema(hidden = true)
    private LocalDateTime createdAt;

    public TaskUpdateDto() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    @Override
    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}