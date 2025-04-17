package com.tinusj.ultima.repository;


import com.tinusj.ultima.dao.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    long count();

    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o")
    Double sumTotalPrice();

    @Query("SELECT DATE(o.orderDate) as date, COUNT(o) as orderCount " +
            "FROM OrderEntity o " +
            "WHERE o.orderDate >= :startDate " +
            "GROUP BY DATE(o.orderDate)")
    List<Object[]> findOrderCountsByDate(LocalDate startDate);
}