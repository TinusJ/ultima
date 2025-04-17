package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Page<TaskEntity> findByStatus(String status, Pageable pageable);

    Page<TaskEntity> findByPriority(String priority, Pageable pageable);

    Page<TaskEntity> findByAssigneeId(Long assigneeId, Pageable pageable);

    List<TaskEntity> findByCreatorId(Long creatorId);
}