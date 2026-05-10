package com.mipt.olgamallina.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.model.Priority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/tasks должен создать задачу")
    void shouldCreateTask() throws Exception {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Controller Task");
        dto.setDescription("desc");
        dto.setPriority(Priority.MEDIUM);
        dto.setDueDate(LocalDateTime.now().plusDays(2));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Controller Task"));
    }

    @Test
    @DisplayName("GET /api/tasks/due-soon должен возвращать список")
    void shouldReturnDueSoon() throws Exception {
        mockMvc.perform(get("/api/tasks/due-soon"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/tasks/bulk-complete должен принимать список id")
    void shouldBulkComplete() throws Exception {
        mockMvc.perform(post("/api/tasks/bulk-complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2,3]"))
                .andExpect(status().is4xxClientError()); // ожидаемо, если id не существуют
    }
}