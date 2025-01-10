package com.LostAndFound.UserService.repository;

import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleEnum roleName);

    boolean existsByRoleName(RoleEnum roleName);
}
