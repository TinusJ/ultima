package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    List<FolderEntity> findByParentFolderIsNull();
    List<FolderEntity> findByParentFolderId(Long parentFolderId);
}