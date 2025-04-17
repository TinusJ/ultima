package com.tinusj.ultima.dao.dto;

public record FileUploadResponseDto(
        Long fileId,
        String fileName,
        String message
) {}