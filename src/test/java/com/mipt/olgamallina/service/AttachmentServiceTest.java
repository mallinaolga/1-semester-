package com.mipt.olgamallina.service;

import com.mipt.olgamallina.exception.FileStorageException;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.repository.InMemoryTaskAttachmentRepository;
import com.mipt.olgamallina.repository.InMemoryTaskRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentServiceTest {

    private AttachmentService attachmentService;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        InMemoryTaskRepository taskRepository = new InMemoryTaskRepository();
        TaskMapper mapper = Mappers.getMapper(TaskMapper.class);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        taskService = new TaskService(taskRepository, mapper, validator);

        Task task = new Task();
        task.setTitle("Test");
        task.setDescription("Test");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setPriority(Priority.MEDIUM);
        task.setTags(Set.of("test"));
        taskRepository.save(task);

        attachmentService = new AttachmentService(
                new InMemoryTaskAttachmentRepository(),
                taskService,
                "test-uploads"
        );
    }

    @Test
    void storeAttachment_shouldSaveMetadata() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                "text/plain",
                "hello world".getBytes()
        );

        var attachment = attachmentService.storeAttachment(1L, file);

        assertNotNull(attachment.getId());
        assertEquals("hello.txt", attachment.getFileName());
        assertEquals("text/plain", attachment.getContentType());
        assertEquals(file.getSize(), attachment.getSize());
    }

    @Test
    void storeAttachment_shouldThrowForEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        assertThrows(FileStorageException.class,
                () -> attachmentService.storeAttachment(1L, file));
    }
}