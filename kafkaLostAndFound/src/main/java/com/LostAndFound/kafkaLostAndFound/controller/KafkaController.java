package com.LostAndFound.kafkaLostAndFound.controller;

import com.LostAndFound.kafkaLostAndFound.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {


    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public KafkaController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam("message") String message)
    {
        kafkaProducerService.sendMessage("my-topic",message);
        return ResponseEntity.ok("Message published to kafka topic ");
    }

}

