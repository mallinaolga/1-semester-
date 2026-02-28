package com.mipt.olgamallina.config;

import com.mipt.olgamallina.repository.StubTaskRepository;
import com.mipt.olgamallina.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for repository beans.
 */
@Configuration
public class RepositoryConfig {

    /**
     * Creates StubTaskRepository as a bean (required by task).
     */
    @Bean(name = "stubTaskRepository")
    public TaskRepository stubTaskRepository() {
        return new StubTaskRepository();
    }
}