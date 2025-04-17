package com.tinusj.ultima.dao.dto;

import java.math.BigDecimal;

public record BestSellerDto(
        Long productId,
        String productName,
        long totalSold,
        BigDecimal totalRevenue
) {}