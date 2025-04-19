package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record BlogPostDto(
        Long id,
        String title,
        String content,
        String thumbnailUrl,
        Set<String> tags,
        Long categoryId,
        String categoryName,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime publishDate,
        boolean isPublished,
        long viewCount
) {}