package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.ContactFormDto;
import com.tinusj.ultima.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Get all contacts", description = "Returns a list of all contacts for the dashboard.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role")
    })

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ContactDto>>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Order order = new Sort.Order(
                sortParams[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return ResponseEntity.ok(com.tinusj.ultima.dao.dto.ApiResponse.ok(contactService.getContacts(pageable)));
    }

    @Operation(summary = "Submit contact form", description = "Submits a contact form with name, email, and message. Public endpoint.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contact form submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ContactDto>> submitContactForm(@RequestBody ContactFormDto formDto) {
        return ResponseEntity.ok(com.tinusj.ultima.dao.dto.ApiResponse.ok(contactService.submitContactForm(formDto)));
    }
}