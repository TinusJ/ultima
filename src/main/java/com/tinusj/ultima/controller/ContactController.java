package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.ContactFormDto;
import com.tinusj.ultima.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Get all contacts", description = "Returns a list of all contacts for the dashboard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ContactDto>> getContacts() {
        return ResponseEntity.ok(contactService.getContacts());
    }

    @Operation(summary = "Submit contact form", description = "Submits a contact form with name, email, and message. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact form submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/submit")
    public ResponseEntity<ContactDto> submitContactForm(@RequestBody ContactFormDto formDto) {
        return ResponseEntity.ok(contactService.submitContactForm(formDto));
    }
}