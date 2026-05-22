package com.mipt.olgamallina.api;

import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.dto.TaskStatusUpdateRequest;
import com.mipt.olgamallina.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse created = taskService.create(request);

        return ResponseEntity
                .created(URI.create("/api/tasks/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable long id) {
        return taskService.get(id);
    }

    @GetMapping
    public List<TaskResponse> findActiveTasks() {
        return taskService.findActiveTasks();
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(
            @PathVariable long id,
            @Valid @RequestBody TaskStatusUpdateRequest request
    ) {
        return taskService.updateStatus(id, request.completed());
    }
}