package com.tinusj.ultima.dao.dto;

public record ContactDto(
        Long id,
        String name,
        String email,
        String phone,
        String position
) {
}