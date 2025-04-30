package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.dao.enums.Role;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.security.JwtTokenProvider;
import com.tinusj.ultima.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!firebase")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterRequestDto registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequestDTO.email());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        user.setRoles(Set.of(Role.USER));

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public String login(LoginRequestDto loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String forgotPassword(String email) {
        // TODO: do not do this as this is a sec violation
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        return "";
    }
}