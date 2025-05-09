package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.TimelineEventDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/timeline")
public class TimelineController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimelineEventDto>>> getTimelineEvents() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getTimelineEvents()));
    }
}
