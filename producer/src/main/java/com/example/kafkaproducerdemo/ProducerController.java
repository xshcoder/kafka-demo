package com.example.kafkaproducerdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce") // Base path for this controller
public class ProducerController {

    private static final Logger log = LoggerFactory.getLogger(ProducerController.class);

    @Autowired
    private MessageProducerService messageProducerService;

    @GetMapping // Handles GET requests to /produce
    public ResponseEntity<String> produceMessages(
            @RequestParam(value = "count", defaultValue = "1") int count) { // Expects a 'count' query parameter

        log.info("Received request to produce {} messages.", count);

        if (count <= 0) {
            return ResponseEntity.badRequest().body("Count parameter must be positive.");
        }

        try {
            // Call the service to send the messages
            messageProducerService.sendMultipleMessages(count);
            String responseMessage = String.format("Successfully triggered sending %d message(s).", count);
            log.info(responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Failed to trigger message sending.", e);
            return ResponseEntity.internalServerError().body("Failed to send messages: " + e.getMessage());
        }
    }
}