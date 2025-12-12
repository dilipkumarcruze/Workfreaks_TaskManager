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

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

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

        // Manual object creation instead of builder
        User newUser = new User();
        newUser.setName(req.getName());
        newUser.setEmail(req.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        newUser.setRole(User.Role.USER);

        userRepository.save(newUser);

        // auto login & return token
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        String token = jwtUtils.generateToken(auth);

        return new JwtResponse(token, "Bearer", newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole().name());
    }
}
