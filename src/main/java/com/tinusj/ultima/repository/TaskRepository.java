package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(String status, Pageable pageable);

    Page<Task> findByPriority(String priority, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    List<Task> findByCreatorId(Long creatorId);
}