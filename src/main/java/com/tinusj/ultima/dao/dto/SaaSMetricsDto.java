package com.tinusj.ultima.dao.dto;

public record SaaSMetricsDto(
        long userCount,
        long subscriptionCount,
        double revenue,
        long visitorCount
) {
}