package com.ajay.spring_security.service;

import com.ajay.spring_security.dto.request.AssignRoleRequest;
import com.ajay.spring_security.dto.response.MessageResponse;
import com.ajay.spring_security.models.Role;

import java.util.List;

public interface RolesService {
    List<Role> getAllRoles();

    MessageResponse addRole(Role role);

    MessageResponse assignRolesToUser(AssignRoleRequest assignRoleRequest);
}
