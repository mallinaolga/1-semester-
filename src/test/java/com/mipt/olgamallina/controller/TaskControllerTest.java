package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TaskController endpoints using TestRestTemplate.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createTask_positive_returns201() {
        Task req = new Task(null, "Test", "Created from test", false);

        ResponseEntity<Task> resp = restTemplate.postForEntity("/api/tasks", req, Task.class);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getId());
        assertEquals("Test", resp.getBody().getTitle());
    }

    @Test
    void createTask_negative_invalidJson_returns400() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{", headers); // broken json

        ResponseEntity<String> resp = restTemplate.exchange("/api/tasks", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    void getAll_positive_returns200() {
        // create one item first
        Task req = new Task(null, "List", "For getAll", false);
        restTemplate.postForEntity("/api/tasks", req, Task.class);

        ResponseEntity<Task[]> resp = restTemplate.getForEntity("/api/tasks", Task[].class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().length >= 1);
    }

    @Test
    void getById_positive_returns200() {
        Task created = restTemplate.postForEntity("/api/tasks",
                new Task(null, "ById", "Fetch by id", false),
                Task.class
        ).getBody();

        assertNotNull(created);

        ResponseEntity<Task> resp = restTemplate.getForEntity("/api/tasks/" + created.getId(), Task.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(created.getId(), resp.getBody().getId());
    }

    @Test
    void getById_negative_returns404() {
        ResponseEntity<Task> resp = restTemplate.getForEntity("/api/tasks/not-exists-123", Task.class);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void update_positive_returns200() {
        Task created = restTemplate.postForEntity("/api/tasks",
                new Task(null, "Old", "Before update", false),
                Task.class
        ).getBody();
        assertNotNull(created);

        Task upd = new Task(created.getId(), "New", "After update", true);

        ResponseEntity<Task> resp = restTemplate.exchange(
                "/api/tasks/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(upd),
                Task.class
        );

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("New", resp.getBody().getTitle());
        assertTrue(resp.getBody().isCompleted());
    }

    @Test
    void update_negative_returns404() {
        Task upd = new Task("x", "New", "After update", true);

        ResponseEntity<Task> resp = restTemplate.exchange(
                "/api/tasks/not-exists-456",
                HttpMethod.PUT,
                new HttpEntity<>(upd),
                Task.class
        );

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void delete_positive_returns204() {
        Task created = restTemplate.postForEntity("/api/tasks",
                new Task(null, "To delete", "Delete me", false),
                Task.class
        ).getBody();
        assertNotNull(created);

        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/tasks/" + created.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void delete_negative_returns404() {
        ResponseEntity<Void> resp = restTemplate.exchange(
                "/api/tasks/not-exists-999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}