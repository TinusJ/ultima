package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record FolderDto(
        Long id,
        String name,
        LocalDateTime createdDate,
        Long parentFolderId
) {}