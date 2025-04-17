package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorEntity, Long> {
    long count();

    @Query("SELECT DATE(v.visitDate) as date, SUM(v.visitCount) as visitCount " +
            "FROM VisitorEntity v " +
            "WHERE v.visitDate >= :startDate " +
            "GROUP BY DATE(v.visitDate)")
    List<Object[]> findVisitorCountsByDate(LocalDate startDate);

    @Query("SELECT v.pageUrl, SUM(v.visitCount) as visitCount " +
            "FROM VisitorEntity v " +
            "GROUP BY v.pageUrl " +
            "ORDER BY visitCount DESC")
    List<Object[]> findMostVisitedPages();

    @Query("SELECT v.source, SUM(v.visitCount) as visitCount " +
            "FROM VisitorEntity v " +
            "GROUP BY v.source " +
            "ORDER BY visitCount DESC")
    List<Object[]> findReferrals();

    @Query("SELECT v.deviceType, (SUM(v.visitCount) * 100.0 / (SELECT SUM(v2.visitCount) FROM VisitorEntity v2)) as percentage " +
            "FROM VisitorEntity v " +
            "WHERE v.deviceType IS NOT NULL " +
            "GROUP BY v.deviceType")
    List<Object[]> findDeviceDistribution();
}
