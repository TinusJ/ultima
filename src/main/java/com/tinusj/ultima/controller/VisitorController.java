package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.VisitorDto;
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
@RequestMapping("/api/v1/visitors")
public class VisitorController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<VisitorDto>> getVisitors() {
        return ResponseEntity.ok(dashboardService.getVisitors());
    }

    @GetMapping("/analytics/graph")
    public ResponseEntity<List<VisitorsGraphDataDto>> getVisitorsGraphData(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(dashboardService.getVisitorsGraphData(date));
    }
}