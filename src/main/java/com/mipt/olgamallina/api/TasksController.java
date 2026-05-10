package com.mipt.olgamallina.api;

import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.service.TasksGatewayService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {
    private final TasksGatewayService service;

    public TasksController(TasksGatewayService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        TaskResponse created = service.create(request);

        if (created.id() != null && created.id() > 0) {
            return ResponseEntity
                    .created(URI.create("/api/v1/tasks/" + created.id()))
                    .body(created);
        }

        return ResponseEntity.status(503).body(created);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @GetMapping
    public List<TaskResponse> list(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "20") Integer limit
    ) {
        return service.list(completed, limit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unstable")
    public ResponseEntity<String> unstable(
            @RequestParam(defaultValue = "500") String mode
    ) {
        return ResponseEntity.ok(service.unstable(mode));
    }
}