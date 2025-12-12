package com.TaskManager.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TaskManager.DTO.JwtResponse;
import com.TaskManager.DTO.LoginRequest;
import com.TaskManager.DTO.SignupRequest;
import com.TaskManager.Service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        JwtResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        JwtResponse res = authService.signup(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
