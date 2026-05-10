package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.dto.TaskResponseDto;
import com.mipt.olgamallina.dto.TaskUpdateDto;
import com.mipt.olgamallina.exception.BulkTaskUpdateException;
import com.mipt.olgamallina.exception.TaskNotFoundException;
import com.mipt.olgamallina.mapper.TaskMapper;
import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDto(saved);
    }

    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));

        taskMapper.update(task, dto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDto(saved);
    }

    public List<TaskResponseDto> getDueInNext7Days() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in7Days = now.plusDays(7);

        return taskRepository.findTasksDueBetween(now, in7Days)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public List<TaskResponseDto> getAllWithAttachments() {
        return taskRepository.findAllWithAttachments()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = BulkTaskUpdateException.class
    )
    public void bulkCompleteTasks(List<Long> ids) {
        List<Task> tasks = taskRepository.findAllById(ids);

        if (tasks.size() != ids.size()) {
            throw new BulkTaskUpdateException("At least one task ID does not exist");
        }

        tasks.forEach(t -> t.setCompleted(true));
        taskRepository.saveAll(tasks);
    }
}