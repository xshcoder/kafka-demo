package com.example.kafkaconsumerdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap; // Added import
import java.util.Map; // Added import

@RestController
@RequestMapping("/consumer") // Base path for this controller
public class ConsumerController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @GetMapping("/consumed-count") // Handles GET requests to /consumer/consumed-count
    public ResponseEntity<Map<String, Long>> getConsumedCounts() { // Return a Map
        long userCount = messageConsumerService.getConsumedUserCount();
        long postCount = messageConsumerService.getConsumedPostCount(); // Get post count

        Map<String, Long> counts = new HashMap<>();
        counts.put("totalUsersConsumed", userCount);
        counts.put("totalPostsProcessedByStream", postCount); // Clarify post count meaning

        // String responseMessage = String.format("Total users consumed: %d, Total posts processed by stream: %d", userCount, postCount);
        return ResponseEntity.ok(counts); // Return JSON map
    }
}