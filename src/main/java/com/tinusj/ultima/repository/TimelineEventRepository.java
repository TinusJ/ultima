package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.TimelineEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineEventRepository extends JpaRepository<TimelineEventEntity, Long> {
}