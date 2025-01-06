package com.LostAndFound.UserService.controller;

import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private Users user,user1;
    private ApiResponse response;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto,userDto1;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto=new UserDto();
        userDto.setUserId(1);
        userDto.setUserName("Ishika");
        userDto.setEmail("ishika@11");
        userDto.setStatus(true);

        userDto1=new UserDto();
        userDto1.setUserId(1);
        userDto1.setUserName("mishika");
        userDto1.setEmail("mishika@11");
        userDto1.setStatus(true);

        user = new Users();
        user.setUserId(1);
        user.setUserName("Ishika");
        user.setEmail("ishika@11");
        user.setStatus(true);

        user1 = new Users();
        user1.setUserId(2);
        user1.setUserName("mishika");
        user1.setEmail("mishika@11");
        user1.setStatus(true);

        response = new ApiResponse.Builder()
                .message("User Successfully Added")
                .statusCode(HttpStatus.CREATED)
                .success(true)
                .build();
    }
    @Test
    void testSaveUser_success() {
        when(userService.saveUser(user)).thenReturn(response);
        ResponseEntity<ApiResponse> response = userController.saveUser(user);
        assertNotNull(response);
        assertTrue(response.getBody().isSuccess());
        assertEquals("User Successfully Added", response.getBody().getMessage());
    }
    @Test
    void getUserByEmail()
    {
        when(userService.getUsers(user.getEmail())).thenReturn(userDto);
        ResponseEntity<UserDto> res = userController.getUserByEmail(user.getEmail());
        assertNotNull(res);
        assertTrue(response.isSuccess());
    }

    @Test
    void getUserById()
    {
        when(userService.getUsers(user.getUserId())).thenReturn(userDto);
        ResponseEntity<UserDto> res = userController.getUserById(user.getUserId());
        assertNotNull(res);
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllUser()
    {
        List<UserDto> list=Arrays.asList(userDto,userDto1);
        when(userService.getUsers()).thenReturn(list);
        ResponseEntity<List<UserDto>> res = userController.getUsers();
        assertNotNull(res);
        assertTrue(response.isSuccess());
    }

    @Test
    void disableUser()
    {
        when(userService.disableUserAccount(user.getEmail())).thenReturn(response);
        ResponseEntity<ApiResponse> resp = userController.disableUser(user.getEmail());
        assertNotNull(resp);
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllDisableUsers()
    {
        UserDto userDto=new UserDto();
        userDto.setUserId(1);
        userDto.setUserName("Ishika");
        userDto.setEmail("ishika@11");
        userDto.setStatus(false);

        List<UserDto> list=Arrays.asList(userDto,userDto1);
        when(userService.getUsers()).thenReturn(list);
        ResponseEntity<List<UserDto>> res = userController.getAllUser_Disable();
        assertNotNull(res);
        assertTrue(response.isSuccess());
    }


    @Test
    void enableUser()
    {
        when(userService.enableUserAccount(user.getEmail())).thenReturn(response);
        ResponseEntity<ApiResponse> resp = userController.enableUser(user.getEmail());
        assertNotNull(resp);
        assertTrue(response.isSuccess());
    }

    @Test
    void updateUser()
    {
        Users user2=new Users();
        user2.setUserName("nishika");
        user2.setContact("943868999");
        when(userService.updateUserInfo(user.getEmail(),user2)).thenReturn(response);
        ResponseEntity<ApiResponse> resp=userController.updateUser(user.getEmail(),user2);
        assertTrue(response.isSuccess());
        assertNotNull(resp);
    }
    @Test
    public void deleteUser()
    {
      when(userService.deleteUser(user.getEmail())).thenReturn(response);
        ResponseEntity<ApiResponse> resp = userController.deleteUser(user.getEmail());
        assertNotNull(resp);
        assertTrue(response.isSuccess());
    }
}
