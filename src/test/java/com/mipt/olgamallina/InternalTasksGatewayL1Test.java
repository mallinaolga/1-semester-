package com.mipt.olgamallina;

import com.mipt.olgamallina.client.ExternalTasksClient;
import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.service.TasksGatewayService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalTasksGatewayL1Test {

    @Test
    void serviceShouldDelegateCreateToClient() {
        ExternalTasksClient fakeClient = new FakeExternalTasksClient();
        TasksGatewayService service = new TasksGatewayService(fakeClient);

        TaskResponse response = service.create(
                new TaskCreateRequest("Task", "Description")
        );

        assertEquals(1L, response.id());
        assertEquals("Task", response.title());
    }

    static class FakeExternalTasksClient extends ExternalTasksClient {
        FakeExternalTasksClient() {
            super(null, null);
        }

        @Override
        public TaskResponse create(TaskCreateRequest request) {
            return new TaskResponse(
                    1L,
                    request.title(),
                    request.description(),
                    false
            );
        }

        @Override
        public TaskResponse get(long id) {
            return new TaskResponse(id, "Task", "Description", false);
        }

        @Override
        public List<TaskResponse> list(Boolean completed, Integer limit) {
            return List.of(new TaskResponse(1L, "Task", "Description", false));
        }

        @Override
        public void delete(long id) {
        }

        @Override
        public String unstable(String mode) {
            return "{}";
        }
    }
}