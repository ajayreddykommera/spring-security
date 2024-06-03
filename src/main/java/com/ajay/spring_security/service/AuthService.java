package com.ajay.spring_security.service;

import com.ajay.spring_security.dto.request.LoginRequest;
import com.ajay.spring_security.dto.request.SignupRequest;
import com.ajay.spring_security.dto.response.JwtResponse;
import com.ajay.spring_security.dto.response.MessageResponse;

public interface  AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signUpRequest);
}
