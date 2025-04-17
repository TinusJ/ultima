package com.tinusj.ultima.dao.dto;

import java.math.BigDecimal;

public record SubscriptionDto(
        Long id,
        String name,
        BigDecimal price,
        Integer maxUsers,
        String description
) {}