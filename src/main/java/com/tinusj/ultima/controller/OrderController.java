package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.OrderGraphDataDto;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {
    private final DashboardService dashboardService;

    @GetMapping("/orders/graph")
    public ResponseEntity<List<OrderGraphDataDto>> getOrderGraphData(
            @RequestParam("startDate") String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(dashboardService.getOrderGraphData(date));
    }
}