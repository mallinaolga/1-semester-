package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTask_shouldReturn201() throws Exception {
        String json = """
                {
                  "title": "Finish homework",
                  "description": "Spring Boot homework",
                  "dueDate": "2099-12-31",
                  "priority": "HIGH",
                  "tags": ["study", "java"]
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-API-Version"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Finish homework"));
    }

    @Test
    void createTask_withInvalidTitle_shouldReturn400() throws Exception {
        String json = """
                {
                  "title": "",
                  "description": "Bad request",
                  "dueDate": "2099-12-31",
                  "priority": "LOW",
                  "tags": []
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    void getAllTasks_shouldReturnXTotalCountHeader() throws Exception {
        String json = """
                {
                  "title": "Task one",
                  "description": "Description",
                  "dueDate": "2099-12-31",
                  "priority": "MEDIUM",
                  "tags": ["test"]
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andExpect(header().exists("X-API-Version"));
    }

    @Test
    void favorites_shouldWorkWithSession() throws Exception {
        String json = """
                {
                  "title": "Favorite task",
                  "description": "Description",
                  "dueDate": "2099-12-31",
                  "priority": "MEDIUM",
                  "tags": ["fav"]
                }
                """;

        var createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post("/api/favorites/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk());
    }

    @Test
    void preferences_shouldSetCookie() throws Exception {
        mockMvc.perform(get("/api/preferences/view"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("viewPreference")));
    }

    @Test
    void uploadAttachment_shouldReturn201() throws Exception {
        String taskJson = """
                {
                  "title": "Task with file",
                  "description": "Description",
                  "dueDate": "2099-12-31",
                  "priority": "HIGH",
                  "tags": ["file"]
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello".getBytes()
        );

        mockMvc.perform(multipart("/api/tasks/1/attachments").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").value("test.txt"));
    }
}