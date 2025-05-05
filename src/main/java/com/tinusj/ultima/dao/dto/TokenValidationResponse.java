package com.tinusj.ultima.dao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenValidationResponse(
        @Schema(description = "Whether the token is valid", example = "true")
        boolean valid,

        @Schema(description = "Username of the authenticated user", example = "john.doe")
        String username,

        @Schema(description = "Expiration time of the token in milliseconds", example = "1620000000000")
        long expiresAt
) {
}