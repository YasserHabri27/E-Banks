package com.ebank.service.impl;

import com.ebank.dto.auth.ChangePasswordRequestDTO;
import com.ebank.dto.auth.LoginRequestDTO;
import com.ebank.dto.auth.LoginResponseDTO;
import com.ebank.entity.User;
import com.ebank.exception.AuthenticationException;
import com.ebank.exception.ValidationException;
import com.ebank.service.AuthenticationService;
import com.ebank.service.UserService;
import com.ebank.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Manual constructor for dependency injection
    public AuthenticationServiceImpl(@Lazy AuthenticationManager authenticationManager,
            UserService userService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Login ou mot de passe erronés");
        }

        User user = userService.findByEmail(request.getEmail());
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());

        String token = jwtUtil.generateToken(userDetails, user.getRole().name());

        return LoginResponseDTO.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .expiresIn(3600000L) // 1 Hour
                .build();
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequestDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new ValidationException("Les mots de passe ne correspondent pas");
        }

        User user = userService.findByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException("Le mot de passe actuel est incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.updateUser(user);
        log.info("Password changed successfully for user {}", email);
    }

    @Override
    public String refreshToken(String oldToken) {
        String email = jwtUtil.extractUsername(oldToken);
        UserDetails userDetails = userService.loadUserByUsername(email);

        if (jwtUtil.isTokenValid(oldToken, userDetails)) {
            User user = userService.findByEmail(email);
            return jwtUtil.generateToken(userDetails, user.getRole().name());
        }
        throw new AuthenticationException("Session invalide, veuillez vous authentifier");
    }
}
