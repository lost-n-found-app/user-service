package com.LostAndFound.UserService.controller;

import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;  // JUnit 5 annotation
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    private UserDto userDto;
    private PasswordUpdateDto passwordUpdateDto;
    private String email;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("Devansh", "patnidevansh05@gmail.com", "5514", "919993100965");
        email = "patnidevansh05@gmail.com";
        passwordUpdateDto = new PasswordUpdateDto("patnidevansh05@gmail.com", "oldPassword", "newPassword", "newPassword");
    }

    @Test
    public void testSaveUser_ValidRequest() {
        UserDto userDto = new UserDto("Devansh", "patnidevansh05@gmail.com", "5514", "919993100965");
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User saved successfully");
        when(userService.saveUser(userDto)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.saveUser(userDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User saved successfully", responseEntity.getBody().getMessage());
        verify(userService, times(1)).saveUser(userDto);
    }

    @Test
    public void testLoginUser_Success() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Login successful");
        when(userService.loginUser(userDto)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.loginUser(userDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login successful", responseEntity.getBody().getMessage());
        verify(userService, times(1)).loginUser(userDto);
    }

    @Test
    public void testLoginUser_Failure() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Login failed");
        when(userService.loginUser(userDto)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.loginUser(userDto);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Login failed", responseEntity.getBody().getMessage());
        verify(userService, times(1)).loginUser(userDto);
    }

    @Test
    public void testUnlockUserAccount_Success() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User account unlocked successfully");
        when(userService.unLockUserAccount(email)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.unlockUserAccount(email);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User account unlocked successfully", responseEntity.getBody().getMessage());
        verify(userService, times(1)).unLockUserAccount(email);
    }

    @Test
    public void testUnlockUserAccount_Failure() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Failed to unlock user account");
        when(userService.unLockUserAccount(email)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.unlockUserAccount(email);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Failed to unlock user account", responseEntity.getBody().getMessage());
        verify(userService, times(1)).unLockUserAccount(email);
    }

    @Test
    public void testUnlockUserAccount_InvalidEmail() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "User not found");
        when(userService.unLockUserAccount(email)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.unlockUserAccount(email);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody().getMessage());
        verify(userService, times(1)).unLockUserAccount(email);
    }

    @Test
    public void testUpdatePassword_Success() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Password updated successfully");
        when(userService.updatePassword(passwordUpdateDto)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.updatePassword(passwordUpdateDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Password updated successfully", responseEntity.getBody().getMessage());
        verify(userService, times(1)).updatePassword(passwordUpdateDto);
    }

    @Test
    public void testUpdatePassword_Failure() {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Invalid password update request");
        when(userService.updatePassword(passwordUpdateDto)).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> responseEntity = userController.updatePassword(passwordUpdateDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid password update request", responseEntity.getBody().getMessage());
        verify(userService, times(1)).updatePassword(passwordUpdateDto);
    }
}



