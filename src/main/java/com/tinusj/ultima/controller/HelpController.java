package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.FaqDto;
import com.tinusj.ultima.service.HelpService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/help")
@RequiredArgsConstructor
public class HelpController {

    private final HelpService helpService;

    @Operation(summary = "List all FAQs", description = "Returns a list of all frequently asked questions with contact information. Public endpoint.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    @GetMapping("/faqs")
    public ResponseEntity<ApiResponse<List<FaqDto>>> getFaqs() {
        return ResponseEntity.ok(ApiResponse.ok(helpService.getFaqs()));
    }

    @Operation(summary = "Search FAQs by keyword", description = "Returns a list of FAQs matching the search keyword. Public endpoint.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    @GetMapping("/faqs/search")
    public ResponseEntity<ApiResponse<List<FaqDto>>> searchFaqs(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(helpService.searchFaqs(keyword)));
    }
}