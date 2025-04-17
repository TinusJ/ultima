package com.tinusj.ultima.dao.dto;

public record AudienceDto(
        Long id,
        String name,
        String ageRange,
        String gender
) {}