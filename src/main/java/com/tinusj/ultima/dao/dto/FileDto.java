package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record FileDto(
        Long id,
        String name,
        Long size,
        String type,
        LocalDateTime uploadDate,
        Long folderId
) {
}