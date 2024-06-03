package com.ajay.spring_security.controller;

import com.ajay.spring_security.dto.request.LoginRequest;
import com.ajay.spring_security.dto.request.SignupRequest;
import com.ajay.spring_security.dto.response.JwtResponse;
import com.ajay.spring_security.dto.response.MessageResponse;
import com.ajay.spring_security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("request to client {}", loginRequest);
        JwtResponse response = authService.authenticateUser(loginRequest);
        log.info("response to client {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("request to client {}", signUpRequest);
        MessageResponse response = authService.registerUser(signUpRequest);
        log.info("response to client {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
