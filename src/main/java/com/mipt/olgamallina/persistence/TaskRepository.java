package com.mipt.olgamallina.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("select t from TaskEntity t where t.completed = false order by t.id asc")
    List<TaskEntity> findActiveTasks();
}