package com.mipt.olgamallina.service;

import com.mipt.olgamallina.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service that demonstrates @Primary and @Qualifier by injecting two repositories.
 */
@Service
public class TaskStatisticsService {

    private final TaskRepository primaryRepository;
    private final TaskRepository stubRepository;

    @Value("${app.name:todo-list-manager}")
    private String appName;

    @Value("${app.version:0.0.1}")
    private String appVersion;

    public TaskStatisticsService(
            TaskRepository primaryRepository,
            @Qualifier("stubTaskRepository") TaskRepository stubRepository
    ) {
        this.primaryRepository = primaryRepository;
        this.stubRepository = stubRepository;
    }

    public String describe() {
        return "app=" + appName + " v" + appVersion
                + " | primaryRepoCount=" + primaryRepository.findAll().size()
                + " | stubRepoCount=" + stubRepository.findAll().size();
    }
}