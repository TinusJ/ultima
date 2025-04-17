package com.tinusj.ultima.dao.dto;

public record BlogPostDto(
        Long id,
        String title,
        Long viewCount
) {}