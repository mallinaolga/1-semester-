package com.mipt.olgamallina.mapper;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    @DisplayName("toEntity должен корректно маппить TaskCreateDto -> Task")
    void shouldMapCreateDtoToEntity() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("New task");
        dto.setDescription("Description");
        dto.setPriority(Priority.HIGH);
        dto.setDueDate(LocalDateTime.now().plusDays(1));
        dto.setTags(Set.of("study", "java"));

        Task task = taskMapper.toEntity(dto);

        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isEqualTo("New task");
        assertThat(task.getDescription()).isEqualTo("Description");
        assertThat(task.isCompleted()).isFalse();
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getTags()).containsExactlyInAnyOrder("study", "java");
    }

    @Test
    @DisplayName("toDto должен корректно маппить Task -> TaskResponseDto")
    void shouldMapEntityToDto() {
        Task task = Task.builder()
                .id(10L)
                .title("Task")
                .description("Desc")
                .completed(true)
                .priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(2))
                .tags(Set.of("work"))
                .build();

        TaskResponseDto dto = taskMapper.toDto(task);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getTitle()).isEqualTo("Task");
        assertThat(dto.isCompleted()).isTrue();
        assertThat(dto.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(dto.getTags()).contains("work");
    }

    @Test
    @DisplayName("update должен частично обновлять сущность")
    void shouldPartiallyUpdateEntity() {
        Task task = Task.builder()
                .id(1L)
                .title("Old")
                .description("Old desc")
                .completed(false)
                .priority(Priority.LOW)
                .build();

        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setTitle("New");
        dto.setCompleted(true);

        taskMapper.update(task, dto);

        assertThat(task.getTitle()).isEqualTo("New");
        assertThat(task.isCompleted()).isTrue();
        assertThat(task.getDescription()).isEqualTo("Old desc");
        assertThat(task.getPriority()).isEqualTo(Priority.LOW);
    }
}