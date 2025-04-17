package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ActivityDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@PreAuthorize("isAuthenticated()")
public class ActivityController {
    private final DashboardService dashboardService;


    @GetMapping("/activities")
    public ResponseEntity<List<ActivityDto>> getActivities() {
        return ResponseEntity.ok(dashboardService.getActivities());
    }
}