package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.LoginRequestEmailDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
    String register(RegisterRequestDto registerRequestDTO);

    String login(LoginRequestDto loginRequestDTO);

    String login(LoginRequestEmailDto loginRequestEmailDto);

    String forgotPassword(@NotBlank @Email String email);

    /**
     * Generate an email verification link for the specified user email.
     */
    String verifyEmail(@NotBlank @Email String email);
}
