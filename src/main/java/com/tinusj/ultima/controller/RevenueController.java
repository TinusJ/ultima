package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.RevenueGraphDataDto;
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
@RequestMapping("/api/v1/revenue")
public class RevenueController {
    private final DashboardService dashboardService;

    @GetMapping("/graph")
    public ResponseEntity<ApiResponse<List<RevenueGraphDataDto>>> getRevenueGraphData(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getRevenueGraphData(date)));
    }
}