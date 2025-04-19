package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.KanbanListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanListRepository extends JpaRepository<KanbanListEntity, Long> {
    List<KanbanListEntity> findByKanbanBoardId(Long boardId);
}