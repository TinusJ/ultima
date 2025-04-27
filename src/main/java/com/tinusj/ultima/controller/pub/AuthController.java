package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.LoginResponseDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import com.tinusj.ultima.dao.dto.RegisterResponseDto;
import com.tinusj.ultima.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "API for user registration and login")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User registered successfully"), @ApiResponse(responseCode = "400", description = "Invalid input data or username/email already exists")})
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDTO) {
        String message = authService.register(registerRequestDTO);
        return ResponseEntity.ok(new RegisterResponseDto(message));
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "JWT token"), @ApiResponse(responseCode = "401", description = "Invalid credentials")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDTO) {

        log.info(loginRequestDTO.toString());
        String token = authService.login(loginRequestDTO);

        return ResponseEntity.ok(new LoginResponseDto(loginRequestDTO.username(), token));
    }
}
