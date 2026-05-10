package com.mipt.olgamallina.repository;

import com.mipt.olgamallina.model.Priority;
import com.mipt.olgamallina.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

    @Query("select t from Task t where t.dueDate between :from and :to")
    List<Task> findTasksDueBetween(LocalDateTime from, LocalDateTime to);

    @EntityGraph(attributePaths = "attachments")
    @Query("select t from Task t")
    List<Task> findAllWithAttachments();
}