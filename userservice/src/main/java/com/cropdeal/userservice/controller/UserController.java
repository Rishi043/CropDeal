package com.cropdeal.userservice.controller;

import com.cropdeal.userservice.repository.UserRepository;
import com.cropdeal.userservice.entity.User;
import com.cropdeal.userservice.service.JWTService;
import com.cropdeal.userservice.service.implementation.JWTServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final JWTServiceImp jwtServiceImp;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public User getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtServiceImp.extractUserName(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
