package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.exception.TaskNotFoundException;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.repository.TaskRepository;
import com.mipt.olgamallina.validation.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final Validator validator;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, Validator validator) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.validator = validator;
    }

    public Task create(TaskCreateDto dto) {
        Task task = taskMapper.toEntity(dto);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task update(Long id, TaskUpdateDto dto) {
        Task existing = getById(id);

        dto.setCreatedAt(existing.getCreatedAt());
        validateDueDateAgainstCreatedAt(dto);

        taskMapper.updateEntity(dto, existing);
        return taskRepository.save(existing);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public long count() {
        return taskRepository.count();
    }

    private void validateDueDateAgainstCreatedAt(TaskUpdateDto dto) {
        Set<ConstraintViolation<TaskUpdateDto>> violations = validator.validate(dto, OnUpdate.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}