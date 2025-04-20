package com.tinusj.ultima.dao.dto;

public record EmailFolderDto(
        Long id,
        String name,
        String type,
        Long userId
) {}