package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
}
