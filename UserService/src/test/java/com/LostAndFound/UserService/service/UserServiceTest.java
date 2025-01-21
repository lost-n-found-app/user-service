package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Role;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.enums.RoleEnum;
import com.LostAndFound.UserService.exceptions.PasswordMismatchException;
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
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;

    private Users user;

    private UserDto createUserDto() {
        return new UserDto("Devansh", "patnidevansh05@gmail.com", "5514", "919993100965");
    }

    private final int max_Attempt;

    {
        max_Attempt = 3;
    }

    private List<ProductDto> createProductList() {
        return Arrays.asList(
                new ProductDto("Devansh", "product1@example.com", "productCode1", "919993100965"),
                new ProductDto("Devansh", "product2@example.com", "productCode2", "919993100965"));
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
    public void saveUserAndReportItem_shouldSaveUserSuccessfully() {
        Role role = new Role();
        role.setRoleName(RoleEnum.ROLE_ADMIN);
        UserDto userDto = new UserDto("Devansh", "patnidevansh05@gmail.com", "5514", "919993100965");
        List<ProductDto> products = Arrays.asList(
                new ProductDto("Product1", "Description1", "Category1", "919993100965"),
                new ProductDto("Product2", "Description2", "Category2", "919993100965"));
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(userRepo.save(Mockito.any(Users.class))).thenAnswer(i -> i.getArgument(0));
        ApiResponse response = userService.saveUserAndReportItem(userDto, products);
        Assertions.assertNotNull(response);
        assertTrue(response.isSuccess());
        Assertions.assertEquals("User Successfully Added", response.getMessage());
        verify(userRepo, times(2)).save(Mockito.any(Users.class));
    }

    @Test
    public void saveUser_shouldThrowUserAlreadyExistsException() {

        Users existingUser = new Users();
        UserDto userDto = createUserDto();
        existingUser.setEmail(userDto.getEmail());
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(existingUser));
        UserAlreadyExistsException exception = Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.saveUserAndReportItem(userDto, createProductList()));
        Assertions.assertEquals("User Failed to Added [EMAIL SHOULD BE UNIQUE]", exception.getMessage());
        verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
        verify(userEventProducer, Mockito.never()).sendUserRegisteredEvent(Mockito.anyString());
        verify(notificationService, Mockito.never())
                .sendSms(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void saveUserAndReportItem_shouldThrowRoleNotFoundException() {
        UserDto userDto = createUserDto();
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepo.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.empty());
        RoleNotFoundException exception = Assertions.assertThrows(
                RoleNotFoundException.class,
                () -> userService.saveUserAndReportItem(userDto, createProductList()));
        Assertions.assertEquals("No Role Found", exception.getMessage());
        verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
        verify(userEventProducer, Mockito.never()).sendUserRegisteredEvent(Mockito.anyString());
        verify(notificationService, Mockito.never())
                .sendSms(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void loginUser_shouldLoginSuccessfully() {
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        assertTrue(response.isSuccess());
        Assertions.assertEquals("User Successfully Login", response.getMessage());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void loginUser_shouldFailWhenEmailNotFound() {
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.loginUser(userDto));
        Assertions.assertEquals("Email is Incorrect ", exception.getMessage());
        verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
    }

    @Test
    public void loginUser_shouldFailWhenPasswordIsIncorrect() {
        userDto.setPassword("wrongPassword");
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        assertFalse(response.isSuccess());
        Assertions.assertEquals("Invalid credentials ,You Have only 3 attempts ", response.getMessage());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void loginUser_shouldLockUserAfterMaxAttempts() {
        user.setLoginAttempts(max_Attempt - 1);
        userDto.setPassword("wrongPassword");
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        assertFalse(response.isSuccess());
        Assertions.assertEquals("Invalid credentials ,You Have only 3 attempts ", response.getMessage());
        assertTrue(user.isLocked());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void loginUser_shouldFailWhenUserIsLocked() {
        user.setLocked(true);
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        ApiResponse response = userService.loginUser(userDto);
        Assertions.assertNotNull(response);
        assertFalse(response.isSuccess());
        Assertions.assertEquals("User account is locked due to too many failed attempts.", response.getMessage());
        verify(userRepo, Mockito.never()).save(user);
    }

    @Test
    public void unLockUserAccount_shouldUnlockAccountSuccessfully() {
        String email = "test@example.com";
        Users user = new Users();
        user.setEmail(email);
        user.setLocked(true);
        user.setLoginAttempts(3);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        ApiResponse response = userService.unLockUserAccount(email);
        Assertions.assertNotNull(response);
        assertTrue(response.isSuccess());
        Assertions.assertEquals("Successfully UnLock User Account", response.getMessage());
        verify(userRepo, times(1)).save(user);
        assertFalse(user.isLocked());
        Assertions.assertEquals(0, user.getLoginAttempts());
    }

    @Test
    public void unLockUserAccount_shouldThrowExceptionWhenEmailNotFound() {
        String email = "nonexistent@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userService.unLockUserAccount(email));
        Assertions.assertEquals("Email is Incorrect ", exception.getMessage());
        verify(userRepo, Mockito.never()).save(Mockito.any(Users.class));
    }

    @Test
    void testHandlePasswordResetRequest_UserExists() {

        String email = "user@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new Users()));
        String mockToken = "123456";
        when(emailService.generateAndStoreToken(email)).thenReturn(mockToken);

        boolean result = userService.handlePasswordResetRequest(email);

        assertTrue(result, "Password reset request should return true for existing user");
      }

    @Test
    void testHandlePasswordResetRequest_UserDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        boolean result = userService.handlePasswordResetRequest(email);
        assertFalse(result, "Password reset request should return false for non-existing user");
        }

    @Test
    void testUpdatePassword_Success() {
        String token = "valid-token";
        String email = "test@example.com";
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto(token, "newPassword123", "newPassword123",token);
        Users user = new Users();
        user.setEmail(email);
        user.setPassword("oldPassword123");
        when(emailService.validateToken(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        ApiResponse response = userService.updatePassword(passwordUpdateDto);
        Assertions.assertEquals("User Password Successfully Updated", response.getMessage());
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_PasswordMismatch() {
        String token = "valid-token";
        String email = "test@example.com";
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto(token, "newPassword123", "differentPassword",token);
        Users user = new Users();
        user.setEmail(email);
        when(emailService.validateToken(anyString())).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        PasswordMismatchException exception = assertThrows(
                PasswordMismatchException.class,
                () -> userService.updatePassword(passwordUpdateDto));
        Assertions.assertEquals("New Password Does Not Match with Re- enter Password", exception.getMessage());
    }
    @Test
    void testUpdatePassword_InvalidToken() {
        String token = "invalid-token";
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto(token, "newPassword123", "newPassword123",token);
        when(emailService.validateToken(token)).thenThrow(new IllegalArgumentException("Token validation failed"));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updatePassword(passwordUpdateDto));
        Assertions.assertEquals("Token validation failed", exception.getMessage());
    }
    @Test
    void testUpdatePassword_UserNotFound() {
        String token = "valid-token";
        String email = "notfound@example.com";
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto(token, "newPassword123", "newPassword123",token);
        when(emailService.validateToken(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updatePassword(passwordUpdateDto));
        Assertions.assertEquals("No User Found By This Email", exception.getMessage());
    }

    @Test
    void testUpdatePassword_SaveUserError() {
        String token = "valid-token";
        String email = "test@example.com";
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto(email, "newPassword123", "newPassword123",token);
        Users user = new Users();
        when(emailService.validateToken(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Database error")).when(userRepo).save(user);
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.updatePassword(passwordUpdateDto));
        Assertions.assertEquals("Database error", exception.getMessage());
       }
}
