package com.ebank.controller;

import com.ebank.dto.auth.ChangePasswordRequestDTO;
import com.ebank.dto.auth.LoginRequestDTO;
import com.ebank.dto.auth.LoginResponseDTO;
import com.ebank.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Login request for email: {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String newToken = authenticationService.refreshToken(token);
            return ResponseEntity.ok(Map.of("token", newToken));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request,
            Principal principal) {
        log.info("Change password request for user: {}", principal.getName());
        authenticationService.changePassword(principal.getName(), request);
        return ResponseEntity.ok().build();
    }
}
