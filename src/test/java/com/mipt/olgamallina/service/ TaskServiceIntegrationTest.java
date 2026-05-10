package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.exception.BulkTaskUpdateException;
import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("create должен создавать задачу")
    void shouldCreateTask() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Task 1");
        dto.setDescription("Desc");
        dto.setPriority(Priority.HIGH);
        dto.setDueDate(LocalDateTime.now().plusDays(2));

        var response = taskService.create(dto);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("update должен обновлять существующую задачу")
    void shouldUpdateTask() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Old title");
        dto.setPriority(Priority.MEDIUM);

        Long id = taskService.create(dto).getId();

        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("New title");
        updateDto.setCompleted(true);

        var updated = taskService.update(id, updateDto);

        assertThat(updated.getTitle()).isEqualTo("New title");
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("bulkCompleteTasks должен откатывать транзакцию, если есть несуществующий id")
    void bulkCompleteShouldRollback() {
        TaskCreateDto dto1 = new TaskCreateDto();
        dto1.setTitle("T1");
        dto1.setPriority(Priority.LOW);

        TaskCreateDto dto2 = new TaskCreateDto();
        dto2.setTitle("T2");
        dto2.setPriority(Priority.HIGH);

        Long id1 = taskService.create(dto1).getId();
        Long id2 = taskService.create(dto2).getId();

        assertThatThrownBy(() -> taskService.bulkCompleteTasks(List.of(id1, id2, 999999L)))
                .isInstanceOf(BulkTaskUpdateException.class);

        // Проверяем, что rollback сработал: обе задачи все еще не completed
        boolean t1Completed = taskRepository.findById(id1).orElseThrow().isCompleted();
        boolean t2Completed = taskRepository.findById(id2).orElseThrow().isCompleted();

        assertThat(t1Completed).isFalse();
        assertThat(t2Completed).isFalse();
    }
}