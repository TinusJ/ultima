package com.tinusj.ultima.dao.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Username is mandatory")
        String username,

        @NotBlank(message = "Password is mandatory")
        String password
) {
}