package com.cropdeal.userservice.controller;

import com.cropdeal.userservice.jwt.JwtService;
import com.cropdeal.userservice.repository.UserRepository;
import com.cropdeal.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public User getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtService.extractEmail(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
