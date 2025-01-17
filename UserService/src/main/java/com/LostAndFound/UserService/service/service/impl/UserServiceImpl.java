package com.LostAndFound.UserService.service.service.impl;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.enums.RoleEnum;
import com.LostAndFound.UserService.exceptions.*;
import com.LostAndFound.UserService.repository.RoleRepository;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.repository.UserRepository;
import com.LostAndFound.UserService.service.NotificationService;
import com.LostAndFound.UserService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    NotificationService notificationService;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserEventProducer userEventProducer;

    private static final int max_Attempt = 3;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ApiResponse saveUserAndReportItem(UserDto userDto, List<ProductDto> products) {
        Optional<Users> isUser = userRepo.findByEmail(userDto.getEmail());
        if (isUser.isPresent()) {
            logger.error("If email Not Found ");
            throw new UserAlreadyExistsException("User Failed to Added [EMAIL SHOULD BE UNIQUE]");
        }
        logger.debug("Fetching role: ROLE_ADMIN");
        Role role = roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> {
            logger.error("Role not found: ROLE_ADMIN");
            return new RoleNotFoundException("No Role Found");
        });
        logger.debug("Creating new user entity for email: {}", userDto.getEmail());
        Users user = new Users();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhoneNumber(userDto.getPhoneNumber());
        String phoneNumber = userDto.getPhoneNumber();
        userRepo.save(user);
        Users save = userRepo.save(user);
        logger.info("User successfully saved with email: {}", userDto.getEmail());
        userEventProducer.sendUserRegisteredEvent("" + user.getUserId());
        notificationService.sendSms(phoneNumber, "Your user has been saved");
        userEventProducer.createUserWithProducts(save.getUserId(), products);
        userEventProducer.sendUserRegisteredEvent("" + user.getUserId());
        return new ApiResponse.Builder()
                .message("User Successfully Added")
                .statusCode(HttpStatus.CREATED)
                .success(true).build();
    }

    @Override
    public ApiResponse loginUser(UserDto userDto) {
        logger.info("Attempting to log in user with email: {}", userDto.getEmail());
        Users user = userRepo.findByEmail(userDto.getEmail()).orElseThrow(() -> {
            logger.error("Login failed: Email not found - {}", userDto.getEmail());
            return new ResourceNotFoundException("Email is Incorrect ");
        });

        if (user.isLocked()) {
            logger.warn("Login failed: User account is locked - {}", userDto.getEmail());
            return new ApiResponse.Builder().message("User account is locked due to too many failed attempts.").statusCode(HttpStatus.FORBIDDEN).success(false).build();
        }

        if (!user.getPassword().equals(userDto.getPassword())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= max_Attempt) {
                user.setLocked(true);
                logger.error("User account locked due to exceeding maximum attempts: {}", userDto.getEmail());
            }
            userRepo.save(user);
            return new ApiResponse.Builder().message("Invalid credentials ,You Have only 3 attempts ").build();
        }
        logger.info("Login successful for email: {}", userDto.getEmail());
        user.setLoginAttempts(0);
        user.setLocked(false);
        userRepo.save(user);
        return new ApiResponse.Builder().message("User Successfully Login").statusCode(HttpStatus.OK).success(true).build();

    }

    @Override
    public ApiResponse unLockUserAccount(String email) {
        logger.info("Attempting to unlock user account with email: {}", email);
        Users user = userRepo.findByEmail(email).orElseThrow(() -> {
            logger.error("Unlock account failed: Email not found - {}", email);
            return new ResourceNotFoundException("Email is Incorrect ");
        });
        user.setLocked(false);
        user.setLoginAttempts(0);
        userRepo.save(user);
        logger.info("Successfully unlocked user account for email: {}", email);
        return new ApiResponse.Builder().message("Successfully UnLock User Account").statusCode(HttpStatus.OK).success(true).build();
    }

    @Override
    public ApiResponse updatePassword(PasswordUpdateDto passwordUpdate) {
        Users users = userRepo.findByEmail(passwordUpdate.getEmail()).orElseThrow(() -> {
            logger.error("Password update failed: No user found for email: {}", passwordUpdate.getEmail());
            return new ResourceNotFoundException("No User Found By This Email");
        });
        if (passwordUpdate.getCurrentPassword() != null && users.getPassword().equals(passwordUpdate.getCurrentPassword())) {
            logger.info("Current password validation successful for email: {}", passwordUpdate.getEmail());

            if (passwordUpdate.getNewPassword().equals(passwordUpdate.getReEnterPassword())) {
                logger.info("New password match validation successful for email: {}", passwordUpdate.getEmail());
                users.setPassword(passwordUpdate.getNewPassword());
                userRepo.save(users);
                userEventProducer.sendPasswordResetEvent("" + users.getUserId());
                logger.info("Password updated successfully for email: {}", passwordUpdate.getEmail());
            } else {
                logger.error("New password does not match re-entered password for email: {}", passwordUpdate.getEmail());
                throw new PasswordMismatchException("New Password Does Not Match with Re- enter Password");
            }
        } else {
            logger.error("Current password is incorrect for email: {}", passwordUpdate.getEmail());
            throw new PasswordMismatchException("Current Password Is Incorrect ");
        }
        return new ApiResponse.Builder().message("User Password Successfully Updated").statusCode(HttpStatus.OK).success(true).build();
    }
}


