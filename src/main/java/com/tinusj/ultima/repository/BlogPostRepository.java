package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.BlogPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    @Query("SELECT p FROM BlogPostEntity p WHERE p.isPublished = true " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BlogPostEntity> findPublishedPosts(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            Pageable pageable);

    List<BlogPostEntity> findByIsPublishedTrue();
}