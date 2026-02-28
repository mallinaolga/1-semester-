package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.scope.RequestScopedBean;
import com.mipt.olgamallina.service.TaskService;
import com.mipt.olgamallina.service.TaskStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller providing CRUD endpoints for Task.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskStatisticsService statisticsService;
    private final RequestScopedBean requestScopedBean;

    public TaskController(TaskService taskService,
                          TaskStatisticsService statisticsService,
                          RequestScopedBean requestScopedBean) {
        this.taskService = taskService;
        this.statisticsService = statisticsService;
        this.requestScopedBean = requestScopedBean;
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable String id) {
        return taskService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        Task created = taskService.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody Task task) {
        return taskService.update(id, task)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = taskService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // extra endpoints for demo (not required, but harmless)
    @GetMapping("/stats")
    public String stats() {
        return statisticsService.describe();
    }

    @GetMapping("/request")
    public String requestInfo() {
        return "requestId=" + requestScopedBean.getRequestId() + " startTime=" + requestScopedBean.getStartTime();
    }
}