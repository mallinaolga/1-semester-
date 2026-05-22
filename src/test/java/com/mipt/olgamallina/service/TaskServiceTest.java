package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.persistence.TaskEntity;
import com.mipt.olgamallina.persistence.TaskRepository;
import com.mipt.olgamallina.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void updateStatusShouldUpdateExistingTaskAndSaveIt() {
        TaskEntity existingTask = new TaskEntity(
                "Write tests",
                "Cover service layer",
                false
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskResponse response = taskService.updateStatus(1L, true);

        assertThat(response.title()).isEqualTo("Write tests");
        assertThat(response.description()).isEqualTo("Cover service layer");
        assertThat(response.completed()).isTrue();

        ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(captor.capture());

        TaskEntity savedTask = captor.getValue();

        assertThat(savedTask.getTitle()).isEqualTo("Write tests");
        assertThat(savedTask.getDescription()).isEqualTo("Cover service layer");
        assertThat(savedTask.isCompleted()).isTrue();
    }
}