package com.ajay.spring_security.service.impl;

import com.ajay.spring_security.dto.request.AssignRoleRequest;
import com.ajay.spring_security.dto.response.MessageResponse;
import com.ajay.spring_security.models.Role;
import com.ajay.spring_security.models.User;
import com.ajay.spring_security.repositories.RoleRepository;
import com.ajay.spring_security.repositories.UserRepository;
import com.ajay.spring_security.service.RolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public MessageResponse addRole(Role request) {
        MessageResponse messageResponse = new MessageResponse();
        String roleName = request.getRoleName().toUpperCase();
        if (roleRepository.existsByRoleName(roleName)) {
            messageResponse.setMessage("Error: Role is already taken!");
            return messageResponse;
        }
        Role role = new Role();
        role.setRoleName(roleName);
        Role response = roleRepository.save(role);
        messageResponse.setMessage("Role [" + response.getRoleName() + "]  Added Successfully");
        return messageResponse;
    }

    @Override
    public MessageResponse assignRolesToUser(AssignRoleRequest assignRoleRequest) {
        MessageResponse response = new MessageResponse();
        Optional<User> userOptional = userRepository.findById(assignRoleRequest.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Role> roles = roleRepository.findByIdIn(assignRoleRequest.getRoleIds());
            user.setRoles(roles);
            userRepository.save(user);
            response.setMessage("Roles assigned successfully");
            return response;
        }
        response.setMessage("User not found");
        return response;
    }
}
