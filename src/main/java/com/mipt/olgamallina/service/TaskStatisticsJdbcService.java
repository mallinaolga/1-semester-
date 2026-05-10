package com.mipt.olgamallina.service;

import com.mipt.olgamallina.dto.PriorityStatsDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatisticsJdbcService {

    private final JdbcTemplate jdbcTemplate;

    public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PriorityStatsDto> getTasksCountByPriority() {
        String sql = """
                select priority, count(*) as cnt
                from tasks
                group by priority
                order by priority
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PriorityStatsDto(rs.getString("priority"), rs.getLong("cnt")));
    }
}