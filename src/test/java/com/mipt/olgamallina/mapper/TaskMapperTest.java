package com.mipt.olgamallina.mapper;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.model.Task;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void toEntity_shouldMapCreateDto() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Task title");
        dto.setDescription("Task description");
        dto.setDueDate(LocalDate.now().plusDays(1));
        dto.setPriority(Priority.HIGH);
        dto.setTags(Set.of("study", "spring"));

        Task task = mapper.toEntity(dto);

        assertNull(task.getId());
        assertEquals("Task title", task.getTitle());
        assertEquals("Task description", task.getDescription());
        assertFalse(task.isCompleted());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(Set.of("study", "spring"), task.getTags());
    }

    @Test
    void updateEntity_shouldIgnoreNulls() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Old title");
        task.setDescription("Old description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setPriority(Priority.LOW);
        task.setTags(new HashSet<>(Set.of("old")));

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("New title");

        mapper.updateEntity(dto, task);

        assertEquals("New title", task.getTitle());
        assertEquals("Old description", task.getDescription());
        assertEquals(Priority.LOW, task.getPriority());
    }

    @Test
    void toResponseDto_shouldMapTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task");
        task.setDescription("Desc");
        task.setCompleted(true);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setPriority(Priority.MEDIUM);
        task.setTags(Set.of("tag1"));

        TaskResponseDto dto = mapper.toResponseDto(task);

        assertEquals(1L, dto.getId());
        assertEquals("Task", dto.getTitle());
        assertEquals("Desc", dto.getDescription());
        assertTrue(dto.isCompleted());
        assertEquals(Priority.MEDIUM, dto.getPriority());
    }
}