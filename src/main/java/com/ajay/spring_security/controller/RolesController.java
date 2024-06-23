package com.ajay.spring_security.controller;

import com.ajay.spring_security.dto.request.AssignRoleRequest;
import com.ajay.spring_security.dto.response.MessageResponse;
import com.ajay.spring_security.models.Role;
import com.ajay.spring_security.service.RolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
public class RolesController {
    private final RolesService rolesService;

    @GetMapping("/getAllRoles")
    @PreAuthorize("hasRole('NORMAL_USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRole() {

        List<Role> response = rolesService.getAllRoles();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/addRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addRole(@RequestBody @Valid Role role) {
        log.info("request from client {}", role);
        MessageResponse response = rolesService.addRole(role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/assignRoles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> assignRolesToUser(@RequestBody @Valid AssignRoleRequest assignRoleRequest) {
        MessageResponse response = rolesService.assignRolesToUser(assignRoleRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
