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
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAttachmentRepository attachmentRepository;

    @Test
    @DisplayName("findByCompletedAndPriority должен вернуть задачи по completed и priority")
    void shouldFindByCompletedAndPriority() {
        Task task = Task.builder()
                .title("High task")
                .description("desc")
                .completed(false)
                .priority(Priority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(1))
                .tags(Set.of("study"))
                .build();

        taskRepository.save(task);

        List<Task> result = taskRepository.findByCompletedAndPriority(false, Priority.HIGH);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("High task");
    }

    @Test
    @DisplayName("findTasksDueBetween должен вернуть задачи в указанном диапазоне")
    void shouldFindTasksDueBetween() {
        Task inRange = Task.builder()
                .title("In range")
                .description("desc")
                .completed(false)
                .priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(3))
                .tags(Set.of("work"))
                .build();

        Task outRange = Task.builder()
                .title("Out range")
                .description("desc")
                .completed(false)
                .priority(Priority.LOW)
                .dueDate(LocalDateTime.now().plusDays(20))
                .tags(Set.of("home"))
                .build();

        taskRepository.save(inRange);
        taskRepository.save(outRange);

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(7);

        List<Task> result = taskRepository.findTasksDueBetween(from, to);

        assertThat(result).extracting(Task::getTitle).contains("In range");
        assertThat(result).extracting(Task::getTitle).doesNotContain("Out range");
    }

    @Test
    @DisplayName("findAllWithAttachments должен загружать задачи вместе с attachments")
    void shouldFindAllWithAttachments() {
        Task task = Task.builder()
                .title("Task with file")
                .description("desc")
                .completed(false)
                .priority(Priority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(2))
                .tags(Set.of("files"))
                .build();

        Task savedTask = taskRepository.save(task);

        TaskAttachment attachment = TaskAttachment.builder()
                .fileName("doc.txt")
                .filePath("/tmp/doc.txt")
                .uploadedAt(LocalDateTime.now())
                .task(savedTask)
                .build();

        attachmentRepository.save(attachment);

        List<Task> result = taskRepository.findAllWithAttachments();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAttachments()).isNotNull();
    }
}