package com.mipt.olgamallina.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("todo_test_db")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findActiveTasksShouldUseRealPostgresContainer() {
        TaskEntity first = new TaskEntity(
                "Active first",
                "Should be returned",
                false
        );

        TaskEntity second = new TaskEntity(
                "Completed",
                "Should not be returned",
                true
        );

        TaskEntity third = new TaskEntity(
                "Active second",
                "Should be returned",
                false
        );

        taskRepository.saveAll(List.of(first, second, third));
        taskRepository.flush();

        List<TaskEntity> activeTasks = taskRepository.findActiveTasks();

        assertThat(activeTasks)
                .hasSize(2)
                .extracting(TaskEntity::getTitle)
                .containsExactly("Active first", "Active second");
    }
}