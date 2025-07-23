package com.cropdeal.userservice.controller;

import com.cropdeal.userservice.dto.AuthRequest;
import com.cropdeal.userservice.dto.AuthResponse;
import com.cropdeal.userservice.entity.User;

import com.cropdeal.userservice.service.AuthService;
import com.cropdeal.userservice.service.AuthenticatationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/auth")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    private final AuthenticatationService authenticatationService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return authenticatationService.signup(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authenticatationService.signin(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        System.out.println("FORGOT PASSWORD REQUEST RECEIVED!");
        System.out.println("Email: " + request.get("email"));

        authService.sendResetLink(request.get("email"));
        return ResponseEntity.ok("Reset password link has been sent to your email");
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody Map<String, String> request) {
        authService.resetPassword(token, request.get("password"));
        return ResponseEntity.ok("Password updated successfully");
    }


}
