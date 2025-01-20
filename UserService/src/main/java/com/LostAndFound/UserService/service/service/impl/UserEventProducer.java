package com.LostAndFound.UserService.service.service.impl;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import com.LostAndFound.UserService.commonClasses.UserProducerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@EnableKafka
@Service
public class UserEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, UserProducerMessage> userKafkaTemplate;

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

    public void createUserWithProducts(UUID userId, List<ProductDto> products) {
        logger.info("Sent password reset event: {}", userId.toString());
        UserProducerMessage userProducerMessage = new UserProducerMessage();
        userProducerMessage.setUserId(userId);
        userProducerMessage.setProducts(products);
        userKafkaTemplate.send("user-product-topic", userProducerMessage);
    }


}
