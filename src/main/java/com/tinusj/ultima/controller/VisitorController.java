package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.VisitorDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visitors")
public class VisitorController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VisitorDto>>> getVisitors() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getVisitors()));
    }
}