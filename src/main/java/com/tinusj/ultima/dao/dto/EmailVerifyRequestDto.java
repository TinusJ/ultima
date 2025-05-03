package com.tinusj.ultima.dao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequestDto(
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email
) {
}