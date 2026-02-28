package com.mipt.olgamallina.repository;

import com.mipt.olgamallina.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations on {@link Task}.
 */
public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(String id);

    Task save(Task task);

    void deleteById(String id);

    boolean existsById(String id);
}