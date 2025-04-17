package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ContactDto;
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
@RequestMapping("/api/v1")
public class ContactController {
    private final DashboardService dashboardService;


    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDto>> getContacts() {
        return ResponseEntity.ok(dashboardService.getContacts());
    }
}