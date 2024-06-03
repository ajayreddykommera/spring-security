package com.ajay.spring_security.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
public class Role {

    @Id
    private String id;
    @NotBlank
    @NotNull
    @Pattern(regexp = "(?i)ROLE_[A-Z]+", message = "Invalid role, It should be like eg: ROLE_NAME")
    private String roleName;
}
