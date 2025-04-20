package com.tinusj.ultima.dao.dto;

import com.tinusj.ultima.dao.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String category,
        List<String> imageUrls,
        ProductStatus status,
        Double averageRating,
        List<ReviewDto> reviews
) {}