package com.ebank.service;

import com.ebank.dto.auth.ChangePasswordRequestDTO;
import com.ebank.dto.auth.LoginRequestDTO;
import com.ebank.dto.auth.LoginResponseDTO;

public interface AuthenticationService {
    LoginResponseDTO authenticate(LoginRequestDTO request);

    void changePassword(String email, ChangePasswordRequestDTO request);

    String refreshToken(String oldToken);
}
