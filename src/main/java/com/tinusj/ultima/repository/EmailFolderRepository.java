package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.EmailFolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailFolderRepository extends JpaRepository<EmailFolderEntity, Long> {
    List<EmailFolderEntity> findByUserId(Long userId);
    Optional<EmailFolderEntity> findByUserIdAndType(Long userId, String type);
}