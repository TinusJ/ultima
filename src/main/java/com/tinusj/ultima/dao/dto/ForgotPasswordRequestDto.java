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
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        @Schema(description = "Registered email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email
) {
}