package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.SubscriptionDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        return ResponseEntity.ok(dashboardService.getSubscriptions());
    }
}