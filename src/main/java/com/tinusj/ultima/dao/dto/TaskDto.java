package com.tinusj.ultima.dao.dto;

public record TaskDto(
        Long id,
        String title,
        String description,
        String status
) {
}
