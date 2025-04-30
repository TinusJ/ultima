package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.InvoiceDto;
import com.tinusj.ultima.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "Get invoice details", description = "Returns the details of a single invoice.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Invoice retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<InvoiceDto>> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(invoiceService.getInvoice(id)));
    }
}