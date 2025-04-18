package com.tinusj.ultima.dao.dto;

public record AnalyticsMetricsDto(
        double revenue,
        long potentialReach,
        long pageViews,
        double engagementRate
) {
}