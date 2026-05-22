package com.mipt.olgamallina.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void createShouldReturn201AndJsonBody() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest(
                "New task",
                "Created from controller test"
        );

        TaskResponse response = new TaskResponse(
                10L,
                "New task",
                "Created from controller test",
                false
        );

        when(taskService.create(any(TaskCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tasks/10"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.description").value("Created from controller test"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getShouldReturn200AndJsonBody() throws Exception {
        TaskResponse response = new TaskResponse(
                5L,
                "Saved task",
                "Task from given part",
                false
        );

        when(taskService.get(5L)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Saved task"))
                .andExpect(jsonPath("$.description").value("Task from given part"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createShouldReturn400WhenTitleIsBlank() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest(
                "",
                "Invalid task"
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateStatusShouldReturn200AndJsonBody() throws Exception {
        TaskResponse response = new TaskResponse(
                7L,
                "Existing task",
                "Task status was updated",
                true
        );

        when(taskService.updateStatus(7L, true)).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/{id}/status", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "completed": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Existing task"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}