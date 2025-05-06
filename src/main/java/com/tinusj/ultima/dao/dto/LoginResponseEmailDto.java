package com.tinusj.ultima.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Response after requesting email login.
 *
 * @param message informational message about the request status
 */
@Schema(description = "Response after email login request")
public record LoginResponseEmailDto(
        @Schema(description = "Result message", example = "Email login link sent to your email")
        String message
) {
}
