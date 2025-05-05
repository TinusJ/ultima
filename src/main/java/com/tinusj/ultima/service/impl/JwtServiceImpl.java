package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.security.JwtTokenProvider;
import com.tinusj.ultima.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public long getExpirationTimeForCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String token) {
            try {
                Date expirationDate = jwtTokenProvider.extractExpiration(token);
                return expirationDate.getTime();
            } catch (Exception e) {
                log.error("Error extracting expiration date from token", e);
            }
        }
        return 0;
    }
}