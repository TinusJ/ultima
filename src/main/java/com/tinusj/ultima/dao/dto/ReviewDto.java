package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long userId,
        String userName
) {}