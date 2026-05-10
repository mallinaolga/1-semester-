package com.mipt.olgamallina.service;

import com.mipt.olgamallina.client.ExternalTasksClient;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.exception.ExternalApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksGatewayService {
    private static final Logger log = LoggerFactory.getLogger(TasksGatewayService.class);

    private final ExternalTasksClient client;

    public TasksGatewayService(ExternalTasksClient client) {
        this.client = client;
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "createFallback")
    public TaskResponse create(TaskCreateRequest request) {
        return client.create(request);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getFallback")
    public TaskResponse get(long id) {
        return client.get(id);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "listFallback")
    public List<TaskResponse> list(Boolean completed, Integer limit) {
        return client.list(completed, limit);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteFallback")
    public void delete(long id) {
        client.delete(id);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "unstableFallback")
    public String unstable(String mode) {
        return client.unstable(mode);
    }

    private TaskResponse createFallback(TaskCreateRequest request, Throwable throwable) {
        log.warn(
                "Fallback create task: title={} cause={}",
                request.title(),
                throwable.toString()
        );

        return new TaskResponse(
                -1L,
                request.title(),
                "Fallback: external API unavailable",
                false
        );
    }

    private TaskResponse getFallback(long id, Throwable throwable) {
        log.warn(
                "Fallback get task: id={} cause={}",
                id,
                throwable.toString()
        );

        return new TaskResponse(
                id,
                "Fallback task",
                "External API unavailable; returned graceful degradation response",
                false
        );
    }

    private List<TaskResponse> listFallback(
            Boolean completed,
            Integer limit,
            Throwable throwable
    ) {
        log.warn(
                "Fallback list tasks: completed={} limit={} cause={}",
                completed,
                limit,
                throwable.toString()
        );

        return List.of(new TaskResponse(
                -1L,
                "Fallback list item",
                "External API unavailable",
                false
        ));
    }

    private void deleteFallback(long id, Throwable throwable) {
        log.warn(
                "Fallback delete task: id={} cause={}",
                id,
                throwable.toString()
        );

        throw new ExternalApiException(
                "Delete fallback: external API unavailable",
                throwable
        );
    }

    private String unstableFallback(String mode, Throwable throwable) {
        log.warn(
                "Fallback unstable call: mode={} cause={}",
                mode,
                throwable.toString()
        );

        return """
                {
                  "message": "fallback response",
                  "mode": "%s",
                  "reason": "%s"
                }
                """.formatted(mode, throwable.getClass().getSimpleName());
    }
}