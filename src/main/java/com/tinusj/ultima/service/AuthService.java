package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.LoginRequestDto;
import com.tinusj.ultima.dao.dto.RegisterRequestDto;

public interface AuthService {
    String register(RegisterRequestDto registerRequestDTO);

    String login(LoginRequestDto loginRequestDTO);
}
