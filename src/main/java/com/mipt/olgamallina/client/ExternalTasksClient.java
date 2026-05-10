package com.mipt.olgamallina.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.olgamallina.dto.ProblemDetails;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.exception.ExternalApiException;
import com.mipt.olgamallina.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExternalTasksClient {
    private static final Logger log = LoggerFactory.getLogger(ExternalTasksClient.class);

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public ExternalTasksClient(
            RestClient externalRestClient,
            ObjectMapper mapper
    ) {
        this.restClient = externalRestClient;
        this.mapper = mapper;
    }

    public TaskResponse create(TaskCreateRequest request) {
        try {
            ResponseEntity<TaskResponse> entity = restClient.post()
                    .uri("/v1/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toEntity(TaskResponse.class);

            URI location = entity.getHeaders().getLocation();

            log.info(
                    "External task created: status={} location={}",
                    entity.getStatusCode(),
                    location
            );

            return entity.getBody();
        } catch (RestClientResponseException exception) {
            throw mapResponseException(exception);
        }
    }

    public TaskResponse get(long id) {
        try {
            return restClient.get()
                    .uri("/v1/tasks/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(TaskResponse.class);
        } catch (RestClientResponseException exception) {
            throw mapResponseException(exception);
        }
    }

    public List<TaskResponse> list(Boolean completed, Integer limit) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/v1/tasks");

                        if (completed != null) {
                            builder.queryParam("completed", completed);
                        }

                        if (limit != null) {
                            builder.queryParam("limit", limit);
                        }

                        return builder.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TaskResponse>>() {});
        } catch (RestClientResponseException exception) {
            throw mapResponseException(exception);
        }
    }

    public void delete(long id) {
        try {
            restClient.delete()
                    .uri("/v1/tasks/{id}", id)
                    .retrieve()
                    .toBodilessEntity();

            log.info("External task deleted: id={}", id);
        } catch (RestClientResponseException exception) {
            throw mapResponseException(exception);
        }
    }

    public String unstable(String mode) {
        try {
            ResponseEntity<String> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/unstable")
                            .queryParam("mode", mode)
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(String.class);

            if (!isJson(response.getHeaders())) {
                throw new ExternalApiException(
                        "Unexpected Content-Type: " + response.getHeaders().getContentType()
                );
            }

            return response.getBody();
        } catch (RestClientResponseException exception) {
            throw mapResponseException(exception);
        }
    }

    private RuntimeException mapResponseException(RestClientResponseException exception) {
        String contentType = exception.getResponseHeaders() == null
                ? null
                : String.valueOf(exception.getResponseHeaders().getContentType());

        String safeBody = safeBody(exception);
        HttpStatusCode status = exception.getStatusCode();

        if (exception instanceof HttpClientErrorException.NotFound) {
            ProblemDetails details = parseProblemDetails(exception);

            throw new TaskNotFoundException(
                    details != null ? details.detail() : "Task not found"
            );
        }

        if (status.value() == 429) {
            String retryAfter = exception.getResponseHeaders() == null
                    ? null
                    : exception.getResponseHeaders().getFirst(HttpHeaders.RETRY_AFTER);

            throw new ExternalApiException(
                    "External API returned 429 Too Many Requests, Retry-After=" + retryAfter,
                    exception
            );
        }

        if (status.is5xxServerError()) {
            log.warn(
                    "External API 5xx: status={} contentType={} body={}",
                    status,
                    contentType,
                    safeBody
            );

            throw new ExternalApiException(
                    "External API server error: " + status.value(),
                    exception
            );
        }

        log.warn(
                "External API HTTP error: status={} contentType={} body={}",
                status,
                contentType,
                safeBody
        );

        throw new ExternalApiException(
                "External API HTTP error: " + status.value(),
                exception
        );
    }

    private boolean isJson(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();

        return contentType == null
                || MediaType.APPLICATION_JSON.includes(contentType)
                || MediaType.APPLICATION_PROBLEM_JSON.includes(contentType);
    }

    private ProblemDetails parseProblemDetails(RestClientResponseException exception) {
        try {
            return mapper.readValue(
                    exception.getResponseBodyAsByteArray(),
                    ProblemDetails.class
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    private String safeBody(RestClientResponseException exception) {
        byte[] body = exception.getResponseBodyAsByteArray();

        if (body == null || body.length == 0) {
            return "";
        }

        String text = new String(body, StandardCharsets.UTF_8)
                .replaceAll("[\\r\\n\\t]+", " ");

        if (text.length() > 300) {
            return text.substring(0, 300) + "...";
        }

        return text;
    }
}