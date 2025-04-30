package com.tinusj.ultima.service.impl;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import com.tinusj.ultima.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Profile("firebase")
@RequiredArgsConstructor
public class AuthServiceFirebaseImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;

    @Override
    public String register(RegisterRequestDto registerRequestDTO) {
        try {
            // Create a Firebase user
            CreateRequest request = new CreateRequest()
                    .setEmail(registerRequestDTO.email())
                    .setPassword(registerRequestDTO.password())
                    .setDisplayName(registerRequestDTO.fullName())
                    .setEmailVerified(false)
                    .setDisabled(false);

            // Additional user properties if needed
            if (registerRequestDTO.phoneNumber() != null) {
                request.setPhoneNumber(registerRequestDTO.phoneNumber());
            }

            // Create the user in Firebase
            UserRecord userRecord = firebaseAuth.createUser(request);

            log.info("Successfully created new user: {}", userRecord.getUid());

            // You might want to add custom claims to the user
            // firebaseAuth.setCustomUserClaims(userRecord.getUid(), Map.of("role", "USER"));

            // Create a custom token for the new user
            return firebaseAuth.createCustomToken(userRecord.getUid());
        } catch (FirebaseAuthException e) {
            log.error("Error registering user: {}", e.getMessage());

            // Handle specific error cases
            if (e.getAuthErrorCode().name().equals("EMAIL_ALREADY_EXISTS")) {
                throw new RuntimeException("Email already in use");
            }

            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public String login(LoginRequestDto loginRequestDTO) {
        try {
            // In Firebase, we typically don't directly verify passwords on the server
            // The client would use Firebase SDK to sign in and get a token
            // This method demonstrates a server-side approach for special cases

            // Get the user by email
            UserRecord userRecord = firebaseAuth.getUserByEmail(loginRequestDTO.email());

            // Note: Firebase Admin SDK doesn't provide direct password verification
            // In a real application, the client would handle authentication directly with Firebase
            // This is a workaround for server-side login (not recommended for production)

            // Create a custom token for the authenticated user
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());

            log.info("Successfully created custom token for user: {}", userRecord.getUid());

            return customToken;
        } catch (FirebaseAuthException e) {
            log.error("Error during login: {}", e.getMessage());

            if (e.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                throw new RuntimeException("Invalid credentials");
            }

            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public String forgotPassword(@NotBlank @Email String email) {
        try {
            // Check if the user exists
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            // Generate password reset link
            String link = firebaseAuth.generatePasswordResetLink(email);

            // In a real application, you would send this link to the user's email
            // using an email service
            log.info("Password reset link generated for user: {}", userRecord.getUid());

            // Return a reference code for tracking (don't return the actual link in production)
            return UUID.randomUUID().toString();
        } catch (FirebaseAuthException e) {
            log.error("Error generating password reset link: {}", e.getMessage());

            if (e.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                // For security reasons, don't reveal if the email exists or not
                // Just return a generic success message
                return UUID.randomUUID().toString();
            }

            throw new RuntimeException("Failed to process password reset: " + e.getMessage());
        }
    }
}