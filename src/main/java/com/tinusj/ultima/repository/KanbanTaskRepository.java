package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.KanbanTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanTaskRepository extends JpaRepository<KanbanTaskEntity, Long> {
    List<KanbanTaskEntity> findByKanbanListId(Long kanbanListId);
    List<KanbanTaskEntity> findByKanbanListKanbanBoardId(Long boardId);
}