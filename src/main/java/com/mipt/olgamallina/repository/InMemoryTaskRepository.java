package com.mipt.olgamallina.repository;

import com.mipt.olgamallina.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link TaskRepository}.
 * Marked as {@link Primary} to be used as the default repository.
 */
@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

    private final ConcurrentHashMap<String, Task> storage = new ConcurrentHashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Task> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Task save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getId() == null || task.getId().isBlank()) {
            throw new IllegalArgumentException("Task id must not be null/blank");
        }
        storage.put(task.getId(), task);
        return task;
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        storage.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) {
            return false;
        }
        return storage.containsKey(id);
    }
}