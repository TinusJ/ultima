package com.tinusj.ultima.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response after requesting email verify.
 *
 * @param message informational message about the request status
 */
@Schema(description = "Response after email verify request")
public record EmailVerifyResponseDto(
        @Schema(description = "Result message", example = "Email verify link sent to your email")
        String message
) {
}
