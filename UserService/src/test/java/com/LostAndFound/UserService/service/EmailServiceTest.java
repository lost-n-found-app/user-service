package com.LostAndFound.UserService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAndStoreOtp() {
        String email = "test@example.com";
        String token = emailService.generateAndStoreOtp(email);
        assertNotNull(token);
        assertEquals(6, token.length());
        Map<String, ?> tokenStore = (Map<String, ?>) getFieldValue(emailService, "tokenStore");
        assertTrue(tokenStore.containsKey(token));
    }

    @Test
    void testValidateToken_ValidOtp() {
        String email = "test@example.com";
        String token = emailService.generateAndStoreOtp(email);
        String result = emailService.validateOtp(token);
        assertEquals(email, result);
        Map<String, ?> tokenStore = (Map<String, ?>) getFieldValue(emailService, "tokenStore");
        assertFalse(tokenStore.containsKey(token)); // Token should be removed after validation
    }

    @Test
    void testValidateToken_InvalidOtp() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.validateOtp("invalid-token"));
        assertEquals("Invalid or expired token", exception.getMessage());
    }

    @Test
    void testValidateToken_ExpiredOtp() throws InterruptedException {
        String email = "test@example.com";
        String token = emailService.generateAndStoreOtp(email);
        var tokenStore = (Map<String, ?>) getFieldValue(emailService, "tokenStore");
        EmailService.OtpInfo otpInfo = (EmailService.OtpInfo) tokenStore.get(token);
        setFieldValue(otpInfo, "expiryTime", LocalDateTime.now().minusSeconds(1));
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> emailService.validateOtp(token));
        assertEquals("Invalid or expired token", exception.getMessage());
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";
        emailService.sendEmail(to, subject, text);
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(text, capturedMessage.getText());
    }

    private Object getFieldValue(Object object, String fieldName) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field value", e);
        }
    }

    private void setFieldValue(Object object, String fieldName, Object value) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }
}
