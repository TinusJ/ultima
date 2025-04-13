package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.LoginRequestDTO;
import com.tinusj.ultima.dao.dto.RegisterRequestDTO;

public interface AuthService {
    String register(RegisterRequestDTO registerRequestDTO);
    String login(LoginRequestDTO loginRequestDTO);
}
