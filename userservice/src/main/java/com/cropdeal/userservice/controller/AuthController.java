package com.cropdeal.userservice.controller;

import com.cropdeal.userservice.dto.AuthRequest;
import com.cropdeal.userservice.dto.AuthResponse;
import com.cropdeal.userservice.entity.User;

import com.cropdeal.userservice.service.AuthenticatationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticatationService authenticatationService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return authenticatationService.signup(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {

        return authenticatationService.signin(request);
        // ✅ Send token in response
    }


}
