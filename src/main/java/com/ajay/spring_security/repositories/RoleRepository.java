package com.ajay.spring_security.repositories;

import com.ajay.spring_security.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);
    Boolean existsByRoleName(String roleName);

    Set<Role> findByIdIn(List<String> roleIds);
}
