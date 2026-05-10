package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.TaskCreateDto;
import com.mipt.olgamallina.model.Priority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TaskStatisticsJdbcServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskStatisticsJdbcService statisticsService;

    @Test
    @DisplayName("getTasksCountByPriority должен возвращать статистику по приоритетам")
    void shouldReturnStatsByPriority() {
        TaskCreateDto d1 = new TaskCreateDto();
        d1.setTitle("A");
        d1.setPriority(Priority.HIGH);
        taskService.create(d1);

        TaskCreateDto d2 = new TaskCreateDto();
        d2.setTitle("B");
        d2.setPriority(Priority.HIGH);
        taskService.create(d2);

        TaskCreateDto d3 = new TaskCreateDto();
        d3.setTitle("C");
        d3.setPriority(Priority.LOW);
        taskService.create(d3);

        Map<String, Long> stats = statisticsService.getTasksCountByPriority()
                .stream()
                .collect(Collectors.toMap(s -> s.getPriority().toUpperCase(), s -> s.getCount()));

        assertThat(stats.get("HIGH")).isGreaterThanOrEqualTo(2L);
        assertThat(stats.get("LOW")).isGreaterThanOrEqualTo(1L);
    }
}