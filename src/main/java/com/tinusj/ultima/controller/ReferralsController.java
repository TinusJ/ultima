package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ReferralDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/referrals")
public class ReferralsController {
    private final DashboardService dashboardService;

    @GetMapping("/analytics")
    public ResponseEntity<List<ReferralDto>> getReferrals() {
        return ResponseEntity.ok(dashboardService.getReferrals());
    }
}