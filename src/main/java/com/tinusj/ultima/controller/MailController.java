package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ComposeEmailDto;
import com.tinusj.ultima.dao.dto.EmailDto;
import com.tinusj.ultima.dao.dto.EmailFolderDto;
import com.tinusj.ultima.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "List user's email folders", description = "Returns a list of email folders for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Folders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/folders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EmailFolderDto>> getFolders() {
        return ResponseEntity.ok(mailService.getFolders());
    }

    @Operation(summary = "List emails in a folder", description = "Returns a paginated list of emails in the specified folder, with optional keyword search.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emails retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    @GetMapping("/emails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EmailDto>> listEmails(
            @RequestParam Long folderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(mailService.listEmails(folderId, search, pageable));
    }

    @Operation(summary = "Get email details", description = "Returns the details of a single email and marks it as read.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @GetMapping("/emails/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailDto> getEmail(@PathVariable Long id) {
        return ResponseEntity.ok(mailService.getEmail(id));
    }

    @Operation(summary = "Compose and send email", description = "Composes a new email and sends it or saves it as a draft.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent or saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/emails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailDto> composeEmail(@RequestBody ComposeEmailDto emailDto) {
        return ResponseEntity.ok(mailService.composeEmail(emailDto));
    }

    @Operation(summary = "Move email to another folder", description = "Moves an email to a different folder.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email moved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Email or folder not found")
    })
    @PutMapping("/emails/{id}/move")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailDto> moveEmail(
            @PathVariable Long id,
            @RequestParam Long newFolderId) {
        return ResponseEntity.ok(mailService.moveEmail(id, newFolderId));
    }

    @Operation(summary = "Mark email as read/unread", description = "Updates the read status of an email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PutMapping("/emails/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailDto> markEmailRead(
            @PathVariable Long id,
            @RequestParam boolean isRead) {
        return ResponseEntity.ok(mailService.markEmailRead(id, isRead));
    }
}