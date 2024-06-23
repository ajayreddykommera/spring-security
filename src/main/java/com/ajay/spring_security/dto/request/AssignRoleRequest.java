package com.ajay.spring_security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class AssignRoleRequest {
    @NotBlank
    @NotNull
    private String userId;
    @NotBlank
    @NotNull
    @Pattern(regexp = "(?i)ROLE_[A-Z]+", message = "Invalid role, It should be like eg: ROLE_NAME")
    private List<String> roleIds;
}
