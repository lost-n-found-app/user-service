package com.LostAndFound.UserService.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegisteredEvent(String userId) {
        String message = "USER_REGISTERED:" + userId;
        try {
            kafkaTemplate.send("user-events", message);
            logger.info("Sent user registered event: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send user registered event: {}", message, e);
        }
    }

    public void sendPasswordResetEvent(String userId) {
        String message = "PASSWORD_RESET:" + userId;
        try {
            kafkaTemplate.send("user-events", message);
            logger.info("Sent password reset event: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send password reset event: {}", message, e);
        }
    }
}
