package com.tinusj.ultima.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload for requesting a password reset.
 *
 * @param email the user's registered email address
 */
@Schema(description = "Request to initiate password reset")
public record ForgotPasswordRequestDto(
        @NotBlank
        @Email
        @Schema(description = "Registered email address", example = "user@example.com", required = true)
        String email
) {
}