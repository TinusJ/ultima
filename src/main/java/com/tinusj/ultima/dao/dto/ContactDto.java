package com.tinusj.ultima.dao.dto;

import java.util.List;

public record ContactDto(
        Long id,
        String name,
        String email,
        String phone,
        String message,
        String avatar,
        List<String> tags
) {}