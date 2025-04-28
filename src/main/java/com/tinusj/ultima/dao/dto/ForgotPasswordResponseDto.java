package com.tinusj.ultima.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response after requesting password reset.
 *
 * @param message informational message about the request status
 */
@Schema(description = "Response after password reset request")
public record ForgotPasswordResponseDto(
        @Schema(description = "Result message", example = "Password reset link sent to your email")
        String message
) {
}