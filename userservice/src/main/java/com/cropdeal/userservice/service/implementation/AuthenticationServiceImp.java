package com.cropdeal.userservice.service.implementation;


import com.cropdeal.userservice.dto.AuthRequest;
import com.cropdeal.userservice.dto.AuthResponse;
import com.cropdeal.userservice.entity.User;
import com.cropdeal.userservice.exception.InvalidCredentialsException;
import com.cropdeal.userservice.exception.UserAlreadyExistsException;
import com.cropdeal.userservice.repository.UserRepository;
import com.cropdeal.userservice.service.AuthenticatationService;
import com.cropdeal.userservice.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticatationService {

    private final UserRepository authUserDAO;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;


    @Override
    public User signup(User signupRequest) {
        if (authUserDAO.existsByEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with username or email");
        }


        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

         return authUserDAO.save(signupRequest);

    }

    @Override
    public AuthResponse signin(AuthRequest signinRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }

        var user = authUserDAO.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        var jwt = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        return response;
    }









}
