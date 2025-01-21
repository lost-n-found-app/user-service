package com.LostAndFound.UserService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.username}")
    private String fromEmail;

    private final Map<String, OtpInfo> otpStore = new ConcurrentHashMap<>();

    public String generateAndStoreOtp(String email) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(otp, new OtpInfo(email, LocalDateTime.now().plusMinutes(15)));
        return otp;
    }

    public String validateOtp(String otp) {
        OtpInfo otpInfo = otpStore.get(otp);
        if (otpInfo == null || otpInfo.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired otp");
        }
        otpStore.remove(otp);
        return otpInfo.getEmail();
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);
        mailSender.send(message);
    }

    public static class OtpInfo {
        private final String email;
        private final LocalDateTime expiryTime;

        public OtpInfo(String email, LocalDateTime expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}