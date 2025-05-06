package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.EmailVerifyRequestDto;
import com.tinusj.ultima.dao.dto.EmailVerifyResponseDto;
import com.tinusj.ultima.dao.dto.ForgotPasswordRequestDto;
import com.tinusj.ultima.dao.dto.ForgotPasswordResponseDto;
import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.LoginRequestEmailDto;
import com.tinusj.ultima.dao.dto.LoginResponseDto;
import com.tinusj.ultima.dao.dto.LoginResponseEmailDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import com.tinusj.ultima.dao.dto.RegisterResponseDto;
import com.tinusj.ultima.dao.dto.TokenValidationResponse;
import com.tinusj.ultima.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public endpoints for authentication: register, login, and forgot password.
 */
@Tag(name = "Authentication", description = "API for user registration, login, and password recovery")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data or username/email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto request
    ) {
        String msg = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(new RegisterResponseDto(msg)));
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "JWT token issued"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        log.info("Login attempt for user: {}", request.email());
        return ResponseEntity.ok(ApiResponse.ok(new LoginResponseDto(request.email(), authService.login(request))));
    }

    @Operation(summary = "Forgot password", description = "Initiates a password reset flow by sending an email.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset email sent"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email format"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No user found with that email")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponseDto>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto request
    ) {
        String msg = authService.forgotPassword(request.email());
        var response = new ForgotPasswordResponseDto(msg);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }

    @Operation(summary = "Email Login", description = "Initiates a email login link by sending an email.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "JWT token issued")
    })
    @PostMapping("/generate-email-sign-in-link")
    public ResponseEntity<ApiResponse<LoginResponseEmailDto>> login(
            @Valid @RequestBody LoginRequestEmailDto request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(new LoginResponseEmailDto(authService.login(request))));
    }

    @Operation(summary = "Generate email verification link",
            description = "Sends a link to verify the user's email.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verification link generated successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email address.")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<EmailVerifyResponseDto>> verifyEmail(@Valid @RequestBody EmailVerifyRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(new EmailVerifyResponseDto(authService.verifyEmail(request.email()))));
    }

    @Operation(
            summary = "Validate JWT Token",
            description = "Validates the JWT token provided in the Authorization header"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token is valid",
                    content = @Content(schema = @Schema(implementation = TokenValidationResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired token",
                    content = @Content
            )
    })
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken() {
        // The actual token is extracted in the JwtAuthenticationFilter
        // If this endpoint is reached, it means the token is valid
        // as it would have been rejected by the filter otherwise

        TokenValidationResponse response = authService.validateCurrentToken();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
