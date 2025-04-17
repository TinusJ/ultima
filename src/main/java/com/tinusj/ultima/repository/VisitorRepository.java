package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorEntity, Long> {
    long count();
}
