package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.enums.RoleEnum;
import com.LostAndFound.UserService.exceptions.ResourceNotFoundException;
import com.LostAndFound.UserService.exceptions.RoleNotFoundException;
import com.LostAndFound.UserService.exceptions.UserAlreadyExistsException;
import com.LostAndFound.UserService.repository.RoleRepository;
import com.LostAndFound.UserService.repository.UserRepository;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.service.service.impl.UserEventProducer;
import com.LostAndFound.UserService.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;

    private Users user;

    private final int max_Attempt;

    {
        max_Attempt = 3;
    }

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUserName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setPhoneNumber("1234567890");

        user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setLoginAttempts(0);
        user.setLocked(false);
    }

    @Test
    public void saveUser_shouldSaveUserSuccessfully() {
        Role role = new Role();
        role.setRoleName(RoleEnum.ROLE_ADMIN);

        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(role));
        Mockito.when(userRepo.save(Mockito.any(Users.class))).thenAnswer(i -> i.getArgument(0));
        ApiResponse response = userService.saveUser(userDto);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("User Successfully Added", response.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any(Users.class));
        Mockito.verify(userEventProducer, Mockito.times(1)).sendUserRegisteredEvent(Mockito.anyString());
        Mockito.verify(notificationService, Mockito.times(1))
                .sendSms(userDto.getPhoneNumber(), "Your user has been saved");
    }

    @Test
    public void saveUser_shouldThrowUserAlreadyExistsException() {

        Users existingUser = new Users();
        existingUser.setEmail(userDto.getEmail());
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(existingUser));
        UserAlreadyExistsException exception = Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.saveUser(userDto));
        Assertions.assertEquals("User Failed to Added [EMAIL SHOULD BE UNIQUE]", exception.getMessage());
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
        Mockito.verify(userEventProducer, Mockito.never()).sendUserRegisteredEvent(Mockito.anyString());
        Mockito.verify(notificationService, Mockito.never())
                .sendSms(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void saveUser_shouldThrowRoleNotFoundException() {
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.empty());
        RoleNotFoundException exception = Assertions.assertThrows(
                RoleNotFoundException.class,
                () -> userService.saveUser(userDto));
        Assertions.assertEquals("No Role Found", exception.getMessage());
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
        Mockito.verify(userEventProducer, Mockito.never()).sendUserRegisteredEvent(Mockito.anyString());
        Mockito.verify(notificationService, Mockito.never())
                .sendSms(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void loginUser_shouldLoginSuccessfully() {
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("User Successfully Login", response.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void loginUser_shouldFailWhenEmailNotFound() {
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.loginUser(userDto));
        Assertions.assertEquals("Email is Incorrect ", exception.getMessage());
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
    }

    @Test
    public void loginUser_shouldFailWhenPasswordIsIncorrect() {
        userDto.setPassword("wrongPassword");
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Invalid credentials ,You Have only 3 attempts ", response.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void loginUser_shouldLockUserAfterMaxAttempts() {
        user.setLoginAttempts(max_Attempt - 1);
        userDto.setPassword("wrongPassword");
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Invalid credentials ,You Have only 3 attempts ", response.getMessage());
        Assertions.assertTrue(user.isLocked());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void loginUser_shouldFailWhenUserIsLocked() {
        user.setLocked(true);
        Mockito.when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("User account is locked due to too many failed attempts.", response.getMessage());
        Mockito.verify(userRepo, Mockito.never()).save(user);
    }

    @Test
    public void unLockUserAccount_shouldUnlockAccountSuccessfully() {
        String email = "test@example.com";
        Users user = new Users();
        user.setEmail(email);
        user.setLocked(true);
        user.setLoginAttempts(3);
        Mockito.when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        ApiResponse response = userService.unLockUserAccount(email);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Successfully UnLock User Account", response.getMessage());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Assertions.assertFalse(user.isLocked());
        Assertions.assertEquals(0, user.getLoginAttempts());
    }

    @Test
    public void unLockUserAccount_shouldThrowExceptionWhenEmailNotFound() {
        String email = "nonexistent@example.com";
        Mockito.when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.unLockUserAccount(email));
        Assertions.assertEquals("Email is Incorrect ", exception.getMessage());
        Mockito.verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
    }
}
