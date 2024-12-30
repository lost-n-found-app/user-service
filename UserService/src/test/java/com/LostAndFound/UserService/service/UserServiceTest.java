package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.exceptions.ResourceNotFoundException;
import com.LostAndFound.UserService.exceptions.UserAlreadyExistsException;
import com.LostAndFound.UserService.repository.IUserRepository;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import javax.xml.transform.OutputKeys;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private Users user, user1;

    private UserDto userDto, userDto1;
    @Mock
    private IUserRepository userRepo;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private ModelMapper mapper;

    private ApiResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setUserId(1);
        user.setUserName("Ishika Jaiswal");
        user.setEmail("ishika@11");
        user.setStatus(true);

        userDto = new UserDto();
        userDto.setUserId(1);
        userDto.setUserName("Ishika Jaiswal");
        userDto.setEmail("ishika@11");


        user1 = new Users();
        user1.setUserId(2);
        user1.setUserName("ishika");

        userDto1 = new UserDto();
        userDto1.setUserId(1);
        userDto1.setUserName("ishika");

        response = new ApiResponse.Builder()
                .message("User Account Disable")
                .statusCode(HttpStatus.OK)
                .success(false)
                .build();


    }

    @Test
    void testGetUsers_success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        UserDto result = userService.getUsers(1);
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getUserName(), result.getUserName());
        verify(userRepo).findById(1);
    }

    @Test
    void testGetUsers_userNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUsers(1);
        });
        assertEquals("No User Found By This Id", exception.getMessage());
    }

    @Test
    void testGetAllUsers_success() {
        List<Users> userList = Arrays.asList(user, user1);
        when(userRepo.findAllStatus(true)).thenReturn(userList);
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(mapper.map(user1, UserDto.class)).thenReturn(userDto1);
        List<UserDto> list = userService.getUsers();
        assertNotNull(list);
        assertEquals(2, list.size());

    }

    @Test
    void testGetUsers_noActiveUsers() {
        when(userRepo.findAllStatus(true)).thenReturn(Collections.emptyList());
        List<UserDto> result = userService.getUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUsersByEmail_success() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        UserDto users = userService.getUsers(user.getEmail());
        assertNotNull(users);
    }

    @Test
    void testGetUsersByEmail_userNotFound() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUsers("jishika"));
        assertEquals("No User Found By This Email", exception.getMessage());
    }

    @Test
    void testDisableUserAccount() {
        response = new ApiResponse.Builder()
                .message("User Account Disable")
                .statusCode(HttpStatus.OK)
                .success(true)
                .build();
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.disableUserAccount(user.getEmail());
        assertFalse(user.isStatus(), "User status should be false after disabling the account");
        assertTrue(response.isSuccess());
    }

    @Test
    void testDisableUserAccount_userNotFound() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.disableUserAccount("jishika"));
        assertEquals("No User Found By This Email", exception.getMessage());
    }

    @Test
    void testEnableUserAccount() {
        user.setStatus(false);
        response.setSuccess(true);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.enableUserAccount(user.getEmail());
        assertTrue(user.isStatus(), "User status should be True after enabling the account");
        assertTrue(response.isSuccess());
    }

    @Test
    void testEnableUserAccount_userNotFound() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.enableUserAccount("jishika"));
        assertEquals("No User Found By This Email", exception.getMessage());
    }

    @Test
    void testDeleteUser() {
        response.setSuccess(true);
        response.setMessage("User Successfully Deleted");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.deleteUser(user.getEmail());
        assertTrue(response.isSuccess());
        assertEquals(response.getMessage(), "User Successfully Deleted");
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser("jishika@11"));
        assertEquals("No User Found By This Email", e.getMessage());


    }

    @Test
    void saveUser()
    {
        response.setSuccess(true);
        response.setMessage("User Successfully Added");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ApiResponse response = userService.saveUser(user);
        assertTrue(response.isSuccess());
        assertEquals(response.getMessage(),"User Successfully Added");
    }

    @Test
    void saveUser_AlreadyExists()
    {
        Users users = new Users();
        users.setUserId(1);
        users.setEmail("jishika@11");
        users.setUserName("Jishika");
        users.setStatus(true);
        users.setRole("USER");

        when(userRepo.findByEmail(users.getEmail())).thenReturn(Optional.of(users));
        assertThrows(UserAlreadyExistsException.class,()-> userService.saveUser(users));
    }

    @Test
    void testUpdateUserInfo_success() {
        response.setSuccess(true);
        Users user2 = new Users();
        user2.setUserId(1);
        user2.setPassword("948838388899");
        user2.setContact("85749648969");
        user2.setAddress("Indore");
        user2.setUserName("Uishika");
        response.setMessage("User Info Successfully Updated");
        when(userRepo.findByEmail("ishika@11")).thenReturn(Optional.of(user));
        ApiResponse result = userService.updateUserInfo("ishika@11", user2);
        assertNotNull(result);
        assertEquals("User Info Successfully Updated", result.getMessage());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.isSuccess());
    }

    @Test
    void testUpdateUserInfo_userNotFound() {
        Users user2 = new Users();
        user2.setUserId(1);
        user2.setPassword("948838388899");
        when(userRepo.findByEmail("jishika@11")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUserInfo("jishika@11", user2);
        });
        assertEquals("No User Found By This Email", exception.getMessage());

    }

    @Test
    void testUpdateUserInfo_noFieldsUpdated() {
        Users user2 = new Users();
        when(userRepo.findByEmail("ishika@11")).thenReturn(Optional.of(user));
        ApiResponse result = userService.updateUserInfo("ishika@11", user2);
        assertNotNull(result);
    }
}
