package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.FolderDto;
import com.tinusj.ultima.dao.entity.FolderEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.FolderRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/folders")
public class FolderController {
    private final DashboardService dashboardService;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FolderDto>> getFolders(@RequestParam(value = "parentFolderId", required = false) Long parentFolderId) {
        return ResponseEntity.ok(dashboardService.getFolders(parentFolderId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FolderDto> createFolder(
            @RequestParam("name") String name,
            @RequestParam(value = "parentFolderId", required = false) Long parentFolderId) {
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(name);
        folderEntity.setCreatedDate(LocalDateTime.now());
        folderEntity.setOwner(getCurrentUser());

        if (parentFolderId != null) {
            FolderEntity parentFolder = folderRepository.findById(parentFolderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found: " + parentFolderId));
            folderEntity.setParentFolder(parentFolder);
        }

        folderRepository.save(folderEntity);

        return ResponseEntity.ok(new FolderDto(folderEntity.getId(), folderEntity.getName(), folderEntity.getCreatedDate(), parentFolderId));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}