package org.leo_s.task_manager_api.controller;

import jakarta.validation.Valid;
import org.leo_s.task_manager_api.core.request.LoginRequest;
import org.leo_s.task_manager_api.core.request.UserRequest;
import org.leo_s.task_manager_api.core.response.AuthResponse;
import org.leo_s.task_manager_api.core.response.UserResponse;
import org.leo_s.task_manager_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}