package com.LostAndFound.UserService.controller;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.dto.UserProductDto;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse> saveUserAndReportItem(@Valid @RequestBody UserProductDto userProduct) {
        UserDto user = userProduct.getUser();
        List<ProductDto> products = userProduct.getProducts();
        ApiResponse response = userService.saveUserAndReportItem(user, products);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/loginUser")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody UserDto userDto) {
        logger.info("Received login attempt for user with email: {}", userDto.getEmail());
        ApiResponse response = userService.loginUser(userDto);
        if (response.getStatusCode() == HttpStatus.OK)
            logger.info("User login successful for email: {}", userDto.getEmail());
        else
            logger.warn("Login failed for user with email: {}", userDto.getEmail());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody PasswordUpdateDto passwordUpdate) {
        logger.info("Received password update request for user with email: {}", passwordUpdate.getEmail());
        ApiResponse response = userService.updatePassword(passwordUpdate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/unlockUserAccount/{email}")
    public ResponseEntity<ApiResponse> unlockUserAccount(@PathVariable String email) {
        logger.info("Received request to unlock user account for email: {}", email);
        ApiResponse response = userService.unLockUserAccount(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email)
    {
        boolean passwordSent=userService.handlePasswordResetRequest(email);
        if(passwordSent)
        {
            return "Email sent";
        }
        throw new RuntimeException("Email not found");
    }

}
