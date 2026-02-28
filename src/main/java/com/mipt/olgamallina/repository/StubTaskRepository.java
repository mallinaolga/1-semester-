package com.mipt.olgamallina.repository;

import com.mipt.olgamallina.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Stub repository with fixed data (configured via @Bean in config).
 */
public class StubTaskRepository implements TaskRepository {

    private final List<Task> fixed = List.of(
            new Task("stub-1", "Stub task 1", "Fixed task from StubTaskRepository", false),
            new Task("stub-2", "Stub task 2", "Another fixed task from StubTaskRepository", true)
    );

    @Override
    public List<Task> findAll() {
        return fixed;
    }

    @Override
    public Optional<Task> findById(String id) {
        return fixed.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public Task save(Task task) {
        // Stub does not persist; return as-is for demo
        return task;
    }

    @Override
    public void deleteById(String id) {
        // no-op
    }

    @Override
    public boolean existsById(String id) {
        return fixed.stream().anyMatch(t -> t.getId().equals(id));
    }
}