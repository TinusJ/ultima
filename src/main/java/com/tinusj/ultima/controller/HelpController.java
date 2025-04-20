package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.FaqDto;
import com.tinusj.ultima.service.HelpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/help")
@RequiredArgsConstructor
public class HelpController {

    private final HelpService helpService;

    @Operation(summary = "List all FAQs", description = "Returns a list of all frequently asked questions with contact information. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    @GetMapping("/faqs")
    public ResponseEntity<List<FaqDto>> getFaqs() {
        return ResponseEntity.ok(helpService.getFaqs());
    }

    @Operation(summary = "Search FAQs by keyword", description = "Returns a list of FAQs matching the search keyword. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    @GetMapping("/faqs/search")
    public ResponseEntity<List<FaqDto>> searchFaqs(@RequestParam String keyword) {
        return ResponseEntity.ok(helpService.searchFaqs(keyword));
    }
}