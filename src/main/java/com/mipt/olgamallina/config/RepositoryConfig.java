
package com.mipt.olgamallina.config;

import com.mipt.olgamallina.repository.StubTaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean(name = "stubTaskRepository")
    public StubTaskRepository stubTaskRepository() {
        return new StubTaskRepository();
    }
}