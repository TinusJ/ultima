package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByFolderIsNull();
    List<FileEntity> findByFolderId(Long folderId);
}