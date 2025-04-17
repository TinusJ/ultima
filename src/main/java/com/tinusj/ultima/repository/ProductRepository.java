package com.tinusj.ultima.repository;

import com.tinusj.ultima.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT p.id, p.name, SUM(o.quantity) as totalSold, SUM(o.totalPrice) as totalRevenue " +
            "FROM OrderEntity o JOIN o.product p " +
            "GROUP BY p.id, p.name " +
            "ORDER BY totalSold DESC")
    List<Object[]> findBestSellers();
}