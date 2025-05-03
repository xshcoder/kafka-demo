package com.example.kafkaconsumerdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer") // Base path for this controller
public class ConsumerController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @GetMapping("/consumed-count") // Handles GET requests to /consumer/consumed-count
    public ResponseEntity<String> getConsumedCount() {
        long count = messageConsumerService.getConsumedMessageCount();
        String responseMessage = String.format("Total messages consumed: %d", count);
        return ResponseEntity.ok(responseMessage);
    }
}