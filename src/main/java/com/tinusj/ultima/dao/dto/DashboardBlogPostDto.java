package com.tinusj.ultima.dao.dto;

public record DashboardBlogPostDto(
        Long id,
        String title,
        long viewCount
) {}