package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record ActivityDto(
        Long id,
        String description,
        LocalDateTime activityDate
) {}