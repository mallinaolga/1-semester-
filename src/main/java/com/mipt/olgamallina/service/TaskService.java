package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskCreateRequest;
import com.mipt.olgamallina.dto.TaskResponse;
import com.mipt.olgamallina.exception.TaskNotFoundException;
import com.mipt.olgamallina.persistence.TaskEntity;
import com.mipt.olgamallina.persistence.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse create(TaskCreateRequest request) {
        TaskEntity task = new TaskEntity(
                request.title(),
                request.description(),
                false
        );

        TaskEntity saved = taskRepository.save(task);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponse get(long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));

        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findActiveTasks() {
        return taskRepository.findActiveTasks()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse updateStatus(long id, boolean completed) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));

        task.setCompleted(completed);

        TaskEntity saved = taskRepository.save(task);

        return toResponse(saved);
    }

    private TaskResponse toResponse(TaskEntity task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}