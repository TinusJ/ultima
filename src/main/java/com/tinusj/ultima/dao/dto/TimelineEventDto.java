package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record TimelineEventDto(
        Long id,
        String title,
        String description,
        LocalDateTime eventDate
) {
}