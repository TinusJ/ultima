package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findByParticipant1IdOrParticipant2Id(Long participant1Id, Long participant2Id);
}