package com.tinusj.ultima.controller.dashboard;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.SaaSMetricsDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard/saas")
public class SaasDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<SaaSMetricsDto>> getSaaSMetrics() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getSaaSMetrics()));
    }
}
