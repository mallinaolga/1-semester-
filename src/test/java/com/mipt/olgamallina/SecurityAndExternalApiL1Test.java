package com.mipt.olgamallina;

import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.external.ExternalApiController;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class SecurityAndExternalApiL1Test {

    @Test
    void externalApiShouldCreateTaskWithLocation() {
        ExternalApiController controller = new ExternalApiController();

        ResponseEntity<TaskResponse> response = controller.create(
                new TaskCreateRequest("Test task", "Test description")
        );

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals("Test task", response.getBody().title());
    }

    @Test
    void externalApiShouldReturnNotFoundProblemDetails() {
        ExternalApiController controller = new ExternalApiController();

        ResponseEntity<?> response = controller.get(999999L);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
}