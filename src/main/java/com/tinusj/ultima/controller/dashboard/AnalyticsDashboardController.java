package com.tinusj.ultima.controller.dashboard;

import com.tinusj.ultima.dao.dto.AnalyticsMetricsDto;
import com.tinusj.ultima.dao.dto.AudienceDto;
import com.tinusj.ultima.dao.dto.BlogPostDto;
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
    public ResponseEntity<List<AudienceDto>> getAudience() {
        return ResponseEntity.ok(dashboardService.getAudience());
    }

    @GetMapping("/blog-posts")
    public ResponseEntity<List<BlogPostDto>> getBlogPosts() {
        return ResponseEntity.ok(dashboardService.getBlogPosts());
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceDto>> getDevices() {
        return ResponseEntity.ok(dashboardService.getDevices());
    }

    @GetMapping("/metrics")
    public ResponseEntity<AnalyticsMetricsDto> getAnalyticsMetrics(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(dashboardService.getAnalyticsMetrics(date));
    }

    @GetMapping("/pages")
    public ResponseEntity<List<MostVisitedPageDto>> getMostVisitedPages() {
        return ResponseEntity.ok(dashboardService.getMostVisitedPages());
    }

    @GetMapping("/referrals")
    public ResponseEntity<List<ReferralDto>> getReferrals() {
        return ResponseEntity.ok(dashboardService.getReferrals());
    }

    @GetMapping("/visitors/graph")
    public ResponseEntity<List<VisitorsGraphDataDto>> getVisitorsGraphData(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(dashboardService.getVisitorsGraphData(date));
    }
}
