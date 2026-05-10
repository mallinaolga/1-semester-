package com.mipt.olgamallina.external;

import com.mipt.olgamallina.dto.ProblemDetails;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {
    private final AtomicLong ids = new AtomicLong(100);
    private final Map<Long, TaskResponse> tasks = new ConcurrentHashMap<>();

    public ExternalApiController() {
        tasks.put(1L, new TaskResponse(
                1L,
                "Initial external task",
                "Created by emulator",
                false
        ));

        tasks.put(2L, new TaskResponse(
                2L,
                "Completed task",
                "Created by emulator",
                true
        ));
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        long id = ids.incrementAndGet();

        TaskResponse created = new TaskResponse(
                id,
                request.title(),
                request.description(),
                false
        );

        tasks.put(id, created);

        return ResponseEntity
                .created(URI.create("/external/v1/tasks/" + id))
                .body(created);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        TaskResponse task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    .body(new ProblemDetails(
                            "about:blank",
                            "Not Found",
                            404,
                            "External task not found: " + id
                    ));
        }

        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks")
    public List<TaskResponse> list(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return tasks.values()
                .stream()
                .filter(task -> completed == null || task.completed() == completed)
                .sorted(Comparator.comparing(TaskResponse::id))
                .limit(Math.max(0, Math.min(limit, 100)))
                .toList();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        tasks.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unstable")
    public ResponseEntity<?> unstable(
            @RequestParam(defaultValue = "500") String mode
    ) throws InterruptedException {
        return switch (mode) {
            case "timeout" -> {
                Thread.sleep(5_000);
                yield ResponseEntity.ok(new TaskResponse(
                        777L,
                        "Too late",
                        "This response should hit client timeout",
                        false
                ));
            }

            case "429" -> ResponseEntity.status(429)
                    .header(HttpHeaders.RETRY_AFTER, "3")
                    .body(new ProblemDetails(
                            "about:blank",
                            "Too Many Requests",
                            429,
                            "External API rate limit"
                    ));

            case "html" -> ResponseEntity.status(502)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><h1>Bad Gateway</h1></body></html>");

            default -> ResponseEntity.status(500)
                    .body(new ProblemDetails(
                            "about:blank",
                            "Internal Server Error",
                            500,
                            "External API failed intentionally"
                    ));
        };
    }
}