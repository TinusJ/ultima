package com.tinusj.ultima.dao.dto;

public record DashboardMetricsDto(
        long orderCount,
        double revenue,
        long customerCount,
        long commentCount
) {}
