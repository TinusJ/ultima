package com.tinusj.ultima.service.impl;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.LoginRequestEmailDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import com.tinusj.ultima.dao.dto.TokenValidationResponse;
import com.tinusj.ultima.security.JwtTokenProvider;
import com.tinusj.ultima.service.AuthService;
import com.tinusj.ultima.service.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("firebase")
public class AuthServiceFirebaseImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequestDto registerRequestDTO) {
        try {
            CreateRequest request = new CreateRequest()
                    .setEmail(registerRequestDTO.email())
                    .setPassword(registerRequestDTO.password())
                    .setDisplayName(registerRequestDTO.fullName())
                    .setEmailVerified(false)
                    .setDisabled(false);

            if (registerRequestDTO.phoneNumber() != null) {
                request.setPhoneNumber(registerRequestDTO.phoneNumber());
            }

            UserRecord userRecord = firebaseAuth.createUser(request);
            log.info("Successfully created new user: {}", userRecord.getUid());

            // Optionally set custom claims:
            firebaseAuth.setCustomUserClaims(userRecord.getUid(), Map.of("role", "USER"));

            return firebaseAuth.createCustomToken(userRecord.getUid());
        } catch (FirebaseAuthException e) {
            log.error("Error registering user: {}", e.getMessage());
            if ("EMAIL_ALREADY_EXISTS".equals(e.getAuthErrorCode().name())) {
                throw new RuntimeException("Email already in use");
            }
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public String login(LoginRequestDto loginRequestDTO) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(loginRequestDTO.email());
            return firebaseAuth.createCustomToken(userRecord.getUid());
        } catch (FirebaseAuthException e) {
            log.error("Error during login: {}", e.getMessage());
            if ("USER_NOT_FOUND".equals(e.getAuthErrorCode().name())) {
                throw new BadCredentialsException("Invalid credentials");
            }
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public String forgotPassword(@NotBlank @Email String email) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            String link = firebaseAuth.generatePasswordResetLink(email);

            log.info("Link: {}", link);
            log.info("Password reset link generated for user: {}", userRecord.getUid());
            return UUID.randomUUID().toString();
        } catch (FirebaseAuthException e) {
            log.error("Error generating password reset link: {}", e.getMessage());
            if ("USER_NOT_FOUND".equals(e.getAuthErrorCode().name())) {
                // For security, do not reveal if the user exists or not
                return UUID.randomUUID().toString();
            }
            throw new RuntimeException("Failed to process password reset: " + e.getMessage());
        }
    }

    /**
     * Generate an email sign-in link for passwordless authentication (web app).
     */
    @Override
    public String login(LoginRequestEmailDto loginRequestEmailDto) {
        try {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                    .setUrl("https://tinusj.com")
                    .setHandleCodeInApp(true)
                    .build();

            String signInLink = firebaseAuth.generateSignInWithEmailLink(loginRequestEmailDto.email(), actionCodeSettings);
            log.info("Email sign-in link generated for user: {}", loginRequestEmailDto.email());

            // In production, send this signInLink via your email service
            return signInLink;
        } catch (FirebaseAuthException e) {
            log.error("Error generating email sign-in link: {}", e.getMessage());
            throw new RuntimeException("Failed to generate email sign-in link: " + e.getMessage());
        }
    }

    @Override
    public String verifyEmail(@NotBlank @Email String email) {
        try {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                    // Your web app's URL, where users will complete verification
                    .setUrl("https://tinusj.com/verifyEmailCallback")
                    .setHandleCodeInApp(true)
                    .build();

            String verificationLink = firebaseAuth.generateEmailVerificationLink(email, actionCodeSettings);
            log.info("Email verification link generated for user: {}", email);

            // TODO: In production, send this verification link via your email service
            return verificationLink;
        } catch (FirebaseAuthException e) {
            log.error("Error generating email verification link: {}", e.getMessage());
            throw new RuntimeException("Failed to generate email verification link: " + e.getMessage());
        }
    }

    @Override
    public TokenValidationResponse validateCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            // Get token expiration from JWT service
            long expiresAt = jwtService.getExpirationTimeForCurrentToken();

            return new TokenValidationResponse(true, username, expiresAt);
        }

        // This should not happen as the endpoint is protected and requires authentication
        throw new IllegalStateException("No authentication found in security context");
    }
}