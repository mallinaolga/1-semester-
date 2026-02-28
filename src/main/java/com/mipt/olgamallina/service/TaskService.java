package com.mipt.olgamallina.service;

import com.mipt.olgamallina.model.Task;
import com.mipt.olgamallina.repository.TaskRepository;
import com.mipt.olgamallina.scope.PrototypeScopedBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service layer for tasks. Demonstrates constructor DI and bean lifecycle annotations.
 */
@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;
    private final ObjectProvider<PrototypeScopedBean> prototypeProvider;

    // cache required by assignment
    private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

    public TaskService(TaskRepository repository, ObjectProvider<PrototypeScopedBean> prototypeProvider) {
        this.repository = repository;
        this.prototypeProvider = prototypeProvider;
    }

    @PostConstruct
    public void initCache() {
        // preload cache from repository
        for (Task t : repository.findAll()) {
            taskCache.put(t.getId(), t);
        }
        log.info("@PostConstruct cache initialized, size={}", taskCache.size());
    }

    @PreDestroy
    public void onDestroy() {
        log.info("@PreDestroy cache size={}", taskCache.size());
    }

    public List<Task> getAll() {
        return repository.findAll();
    }

    public Optional<Task> getById(String id) {
        return repository.findById(id);
    }

    public Task create(Task task) {
        if (task.getId() == null || task.getId().isBlank()) {
            // demonstrate prototype bean usage (new instance each call)
            String newId = prototypeProvider.getObject().generateTaskId();
            task.setId(newId);
        }
        Task saved = repository.save(task);
        taskCache.put(saved.getId(), saved);
        return saved;
    }

    public Optional<Task> update(String id, Task updated) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        updated.setId(id);
        Task saved = repository.save(updated);
        taskCache.put(saved.getId(), saved);
        return Optional.of(saved);
    }

    public boolean delete(String id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        taskCache.remove(id);
        return true;
    }
}