package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.FileDto;
import com.tinusj.ultima.dao.dto.FileUploadResponseDto;
import com.tinusj.ultima.dao.entity.FileEntity;
import com.tinusj.ultima.dao.entity.FolderEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.FileRepository;
import com.tinusj.ultima.repository.FolderRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.DashboardService;
import com.tinusj.ultima.service.impl.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final DashboardService dashboardService;
    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FileDto>>> getFiles(@RequestParam(value = "folderId", required = false) Long folderId) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getFiles(folderId)));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FileUploadResponseDto>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderId", required = false) Long folderId) {
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFileName;
        String filePath = fileStorageService.storeFile(file, fileName);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(originalFileName);
        fileEntity.setPath(filePath);
        fileEntity.setSize(file.getSize());
        fileEntity.setType(file.getContentType());
        fileEntity.setUploadDate(LocalDateTime.now());
        fileEntity.setOwner(getCurrentUser());

        if (folderId != null) {
            FolderEntity folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + folderId));
            fileEntity.setFolder(folder);
        }

        fileRepository.save(fileEntity);

        return ResponseEntity.ok(ApiResponse.ok(new FileUploadResponseDto(fileEntity.getId(), originalFileName, "File uploaded successfully")));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws Exception {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + id));
        Path filePath = fileStorageService.getFilePath(id, Paths.get(fileEntity.getPath()).getFileName().toString());
        byte[] fileContent = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"")
                .contentType(MediaType.parseMediaType(fileEntity.getType()))
                .body(fileContent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + id));
        fileStorageService.deleteFile(Paths.get(fileEntity.getPath()).getFileName().toString());
        fileRepository.delete(fileEntity);
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}