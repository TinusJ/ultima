package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
    @Query("SELECT f FROM FaqEntity f WHERE " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FaqEntity> findByKeyword(@Param("keyword") String keyword);
}