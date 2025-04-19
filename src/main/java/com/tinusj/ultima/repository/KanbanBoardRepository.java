package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.KanbanBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanBoardRepository extends JpaRepository<KanbanBoardEntity, Long> {
    List<KanbanBoardEntity> findByOwnerId(Long ownerId);
}