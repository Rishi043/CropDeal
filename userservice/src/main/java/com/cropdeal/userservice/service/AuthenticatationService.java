package com.cropdeal.userservice.service;


import com.cropdeal.userservice.dto.AuthRequest;
import com.cropdeal.userservice.dto.AuthResponse;
import com.cropdeal.userservice.entity.User;

public interface AuthenticatationService {
    User signup(User signupRequest);


    AuthResponse signin(AuthRequest signinRequest);


}

