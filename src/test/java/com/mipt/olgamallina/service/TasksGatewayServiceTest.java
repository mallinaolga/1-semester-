package com.mipt.olgamallina.service;

import com.mipt.olgamallina.client.ExternalTasksClient;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TasksGatewayServiceTest {

    @Autowired
    private TasksGatewayService tasksGatewayService;

    @MockBean
    private ExternalTasksClient externalTasksClient;

    @Test
    void createShouldDelegateRequestToExternalClient() {
        TaskCreateRequest request = new TaskCreateRequest(
                "Write tests",
                "Cover service layer"
        );

        TaskResponse expected = new TaskResponse(
                1L,
                "Write tests",
                "Cover service layer",
                false
        );

        when(externalTasksClient.create(request)).thenReturn(expected);

        TaskResponse actual = tasksGatewayService.create(request);

        assertThat(actual).isEqualTo(expected);

        ArgumentCaptor<TaskCreateRequest> captor =
                ArgumentCaptor.forClass(TaskCreateRequest.class);

        verify(externalTasksClient).create(captor.capture());

        assertThat(captor.getValue().title()).isEqualTo("Write tests");
        assertThat(captor.getValue().description()).isEqualTo("Cover service layer");
    }
}