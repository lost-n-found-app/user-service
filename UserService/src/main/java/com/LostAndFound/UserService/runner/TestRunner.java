package com.LostAndFound.UserService.runner;

import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.enums.RoleEnum;
import com.LostAndFound.UserService.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepo;

    @Override
    public void run(String... args) throws Exception {
        for (RoleEnum roleEnum1 : RoleEnum.values()) {
            if (!roleRepo.existsByRoleName(roleEnum1)) {
                Role role = new Role();
                role.setRoleName(roleEnum1);
                roleRepo.save(role);
            }
        }
    }
}
