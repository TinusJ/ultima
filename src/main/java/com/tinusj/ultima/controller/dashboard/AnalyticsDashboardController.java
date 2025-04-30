package com.tinusj.ultima.controller.dashboard;

import com.tinusj.ultima.dao.dto.AnalyticsMetricsDto;
import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.AudienceDto;
import com.tinusj.ultima.dao.dto.DashboardBlogPostDto;
import com.tinusj.ultima.dao.dto.DeviceDto;
import com.tinusj.ultima.dao.dto.MostVisitedPageDto;
import com.tinusj.ultima.dao.dto.ReferralDto;
import com.tinusj.ultima.dao.dto.VisitorsGraphDataDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard/analytics")
public class AnalyticsDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/audience")
    public ResponseEntity<ApiResponse<List<AudienceDto>>> getAudience() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getAudience()));
    }

    @GetMapping("/blog-posts")
    public ResponseEntity<ApiResponse<List<DashboardBlogPostDto>>> getBlogPosts() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getBlogPosts()));
    }

    @GetMapping("/devices")
    public ResponseEntity<ApiResponse<List<DeviceDto>>> getDevices() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDevices()));
    }

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<AnalyticsMetricsDto>> getAnalyticsMetrics(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getAnalyticsMetrics(date)));
    }

    @GetMapping("/pages")
    public ResponseEntity<ApiResponse<List<MostVisitedPageDto>>> getMostVisitedPages() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getMostVisitedPages()));
    }

    @GetMapping("/referrals")
    public ResponseEntity<ApiResponse<List<ReferralDto>>> getReferrals() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getReferrals()));
    }

    @GetMapping("/visitors/graph")
    public ResponseEntity<ApiResponse<List<VisitorsGraphDataDto>>> getVisitorsGraphData(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getVisitorsGraphData(date)));
    }
}
