package com.ajay.spring_security.service.impl;

import com.ajay.spring_security.dto.request.LoginRequest;
import com.ajay.spring_security.dto.request.SignupRequest;
import com.ajay.spring_security.dto.response.JwtResponse;
import com.ajay.spring_security.dto.response.MessageResponse;
import com.ajay.spring_security.models.Role;
import com.ajay.spring_security.models.User;
import com.ajay.spring_security.repositories.RoleRepository;
import com.ajay.spring_security.repositories.UserRepository;
import com.ajay.spring_security.security.jwt.JwtUtils;
import com.ajay.spring_security.security.services.UserDetailsImpl;
import com.ajay.spring_security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        log.info("came till here");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate jwt token
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        // Generate refresh token
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        log.info("jwtToken generated...");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwtToken);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setRoles(roles);
        jwtResponse.setEmail(userDetails.getEmail());
        return jwtResponse;
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        MessageResponse response = new MessageResponse();
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            response.setMessage("Error: Username is already taken!");
            return response;
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            response.setMessage("Error: Email is already in use!");
            return response;
        }

        User user = new User();
        user.setUserName(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setFullName(signUpRequest.getFullName());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleName("ROLE_NORMAL_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {


            for (String roleStr : strRoles) {
                try {
                    Role role = roleRepository.findByRoleName(roleStr.toUpperCase())
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(role);

                } catch (IllegalArgumentException e) {
                    response.setMessage("Error: Role - " + roleStr + " not found.");
                    return response;
                }
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
        response.setMessage("User registered successfully!");
        return response;
    }
}
