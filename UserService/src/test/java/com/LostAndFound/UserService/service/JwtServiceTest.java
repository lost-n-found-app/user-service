package com.LostAndFound.UserService.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    public void validateToken_shouldReturnTrueForValidToken() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("1234567890");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));
        JwtService spyJwtService = Mockito.spy(jwtService);
        doReturn(claims).when(spyJwtService).extractAllClaims(anyString());
        String validToken = "validToken";
        boolean isValid = spyJwtService.validateToken(validToken, "1234567890");
        assertTrue(isValid);
    }

    @Test
    public void validateToken_shouldReturnFalseForInvalidPhoneNumber() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("1234567890"); // Mock subject to return a phone number
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000)); // Mock non-expired token
        JwtService spyJwtService = Mockito.spy(jwtService);
        doReturn(claims).when(spyJwtService).extractAllClaims(anyString());
        String validToken = "validToken";
        boolean isValid = spyJwtService.validateToken(validToken, "0987654321"); // Invalid phone number
        assertFalse(isValid);
    }

    @Test
    public void validateToken_shouldReturnFalseForExpiredToken() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("1234567890"); // Mock subject to return a phone number
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 10000)); // Mock expired token
        JwtService spyJwtService = Mockito.spy(jwtService);
        doReturn(claims).when(spyJwtService).extractAllClaims(anyString());
        String validToken = "validToken";
        boolean isValid = spyJwtService.validateToken(validToken, "1234567890");
        assertFalse(isValid);
    }

    @Test
    public void extractNumber_shouldReturnCorrectPhoneNumber() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("1234567890"); // Mock subject to return a phone number
        JwtService spyJwtService = Mockito.spy(jwtService);
        doReturn(claims).when(spyJwtService).extractAllClaims(anyString());
        String token = "validToken";
        String phoneNumber = spyJwtService.extractNumber(token);
        assertEquals("1234567890", phoneNumber);
    }
}
