package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.EmailEntity;
import com.tinusj.ultima.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    @Query("SELECT e FROM EmailEntity e WHERE e.folder.id = :folderId " +
            "AND (:keyword IS NULL OR LOWER(e.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.body) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<EmailEntity> findByFolderId(
            @Param("folderId") Long folderId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT e FROM EmailEntity e WHERE e.folder.user.id = :userId " +
            "AND (e.recipientsTo IS NOT EMPTY AND :user MEMBER OF e.recipientsTo " +
            "OR e.recipientsCc IS NOT EMPTY AND :user MEMBER OF e.recipientsCc " +
            "OR e.recipientsBcc IS NOT EMPTY AND :user MEMBER OF e.recipientsBcc)")
    Page<EmailEntity> findReceivedEmailsByUser(
            @Param("userId") Long userId,
            @Param("user") User user,
            Pageable pageable);
}