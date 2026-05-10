package com.mipt.olgamallina.repository;

import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.model.TaskAttachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.profiles.active=test")
class TaskAttachmentRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAttachmentRepository attachmentRepository;

    @Test
    @DisplayName("findByTaskId должен вернуть вложения задачи")
    void shouldFindByTaskId() {
        Task task = Task.builder()
                .title("Task")
                .description("desc")
                .completed(false)
                .priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(1))
                .tags(Set.of("tag"))
                .build();

        Task savedTask = taskRepository.save(task);

        TaskAttachment a1 = TaskAttachment.builder()
                .fileName("a1.txt")
                .filePath("/tmp/a1.txt")
                .uploadedAt(LocalDateTime.now())
                .task(savedTask)
                .build();

        TaskAttachment a2 = TaskAttachment.builder()
                .fileName("a2.txt")
                .filePath("/tmp/a2.txt")
                .uploadedAt(LocalDateTime.now())
                .task(savedTask)
                .build();

        attachmentRepository.save(a1);
        attachmentRepository.save(a2);

        List<TaskAttachment> result = attachmentRepository.findByTaskId(savedTask.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TaskAttachment::getFileName).containsExactlyInAnyOrder("a1.txt", "a2.txt");
    }
}