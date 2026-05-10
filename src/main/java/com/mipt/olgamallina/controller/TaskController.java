package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDto create(@Valid @RequestBody TaskCreateDto dto) {
        return taskService.create(dto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDto update(@PathVariable Long id, @RequestBody TaskUpdateDto dto) {
        return taskService.update(id, dto);
    }

    @GetMapping("/due-soon")
    public List<TaskResponseDto> getDueSoon() {
        return taskService.getDueInNext7Days();
    }

    @PostMapping("/bulk-complete")
    public void bulkComplete(@RequestBody List<Long> ids) {
        taskService.bulkCompleteTasks(ids);
    }

    @GetMapping("/with-attachments")
    public List<TaskResponseDto> getAllWithAttachments() {
        return taskService.getAllWithAttachments();
    }
}