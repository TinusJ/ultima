package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.AnalyticsMetricsDto;
import com.tinusj.ultima.dao.dto.DashboardMetricsDto;
import com.tinusj.ultima.dao.dto.SaaSMetricsDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/metrics")
public class MetricsController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardMetricsDto> getDashboardMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }

    @GetMapping("/saas")
    public ResponseEntity<SaaSMetricsDto> getSaaSMetrics() {
        return ResponseEntity.ok(dashboardService.getSaaSMetrics());
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsMetricsDto> getAnalyticsMetrics(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(dashboardService.getAnalyticsMetrics(date));
    }
}