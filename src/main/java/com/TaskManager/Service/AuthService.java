package com.TaskManager.Service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TaskManager.DTO.JwtResponse;
import com.TaskManager.DTO.LoginRequest;
import com.TaskManager.DTO.SignupRequest;
import com.TaskManager.Entity.User;
import com.TaskManager.Repository.UserRepository;
import com.TaskManager.Security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtils.generateToken(auth);

        return new JwtResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public JwtResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User newUser = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.USER)
                .build();
        userRepository.save(newUser);

        // auto login & return token
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        String token = jwtUtils.generateToken(auth);

        return new JwtResponse(token, "Bearer", newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole().name());
    }
}
