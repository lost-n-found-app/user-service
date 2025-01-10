package com.LostAndFound.UserService.service.service.impl;

import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.enums.RoleEnum;
import com.LostAndFound.UserService.exceptions.*;
import com.LostAndFound.UserService.repository.RoleRepository;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.repository.UserRepository;
import com.LostAndFound.UserService.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    ModelMapper mapper;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRepository userRepo;

    private static final int max_Attempt = 3;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Override
    public ApiResponse saveUser(UserDto userDto) {
        Optional<Users> users = userRepo.findByEmail(userDto.getEmail());
        if (users.isPresent()) {
            throw new UserAlreadyExistsException("User Failed to Added [EMAIL SHOULD BE UNIQUE]");
        }
        Role role = roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException("No "));
        Users user = new Users();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
        return new ApiResponse.Builder()
                .message("User Successfully Added")
                .statusCode(HttpStatus.CREATED)
                .success(true).build();
    }

    @Override
    public ApiResponse loginUser(UserDto userDto) {
        Users user = userRepo.findByEmail(userDto.getEmail()).
                orElseThrow(() -> new ResourceNotFoundException("Email is Incorrect "));
        if (user.isLocked()) {
            return new ApiResponse.Builder()
                    .message("User account is locked due to too many failed attempts.")
                    .statusCode(HttpStatus.FORBIDDEN)
                    .success(false)
                    .build();
        }
        if (!user.getPassword().equals(userDto.getPassword())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= max_Attempt)
                user.setLocked(true);
            userRepo.save(user);
            return new ApiResponse.Builder()
                    .message("Invalid credentials ,You Have only 3 attempts ")
                    .build();
        }
        user.setLoginAttempts(0);
        user.setLocked(false);
        userRepo.save(user);
        return new ApiResponse.Builder()
                .message("User Successfully Login")
                .statusCode(HttpStatus.OK)
                .success(true).build();

    }

    @Override
    public ApiResponse unLockUserAccount(String email) {
        Users user = userRepo.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException("Email is Incorrect "));
        user.setLocked(false);
        user.setLoginAttempts(0);
        userRepo.save(user);
        return new ApiResponse.Builder().message("Successfully UnLock User Account")
                .statusCode(HttpStatus.OK).success(true).build();
    }

    @Override
    public ApiResponse updatePassword(PasswordUpdateDto passwordUpdate) {
        Users users = userRepo.findByEmail(passwordUpdate.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
        if (passwordUpdate.getCurrentPassword() != null &&
                users.getPassword().equals(passwordUpdate.getCurrentPassword())) {
            System.out.println(passwordUpdate.getNewPassword().equals(passwordUpdate.getReEnterPassword()));
            if (passwordUpdate.getNewPassword().equals(passwordUpdate.getReEnterPassword())) {
                users.setPassword(passwordUpdate.getNewPassword());
                userRepo.save(users);
            } else
                throw new PasswordMismatchException("New Password Does Not Match with Re- enter Passowrd");
        } else
            throw new PasswordMismatchException("Current Password Is Incorrect ");
        userRepo.save(users);
        return new ApiResponse.Builder()
                .message("User Password Successfully Updated")
                .statusCode(HttpStatus.OK)
                .success(true).build();
    }
}


