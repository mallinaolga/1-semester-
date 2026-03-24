package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.service.TaskService;
import com.mipt.olgamallina.validation.OnCreate;
import com.mipt.olgamallina.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Tasks returned")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll() {
        List<TaskResponseDto> response = taskService.getAll().stream()
                .map(taskMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskService.count()))
                .body(response);
    }

    @Operation(summary = "Get task by id")
    @ApiResponse(responseCode = "200", description = "Task found")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return ResponseEntity.ok(taskMapper.toResponseDto(task));
    }

    @Operation(summary = "Create a new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(
            @Validated(OnCreate.class) @RequestBody TaskCreateDto dto) {
        Task created = taskService.create(dto);
        return ResponseEntity.status(201).body(taskMapper.toResponseDto(created));
    }

    @Operation(summary = "Update task")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody TaskUpdateDto dto) {
        Task updated = taskService.update(id, dto);
        return ResponseEntity.ok(taskMapper.toResponseDto(updated));
    }

    @Operation(summary = "Delete task")
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}