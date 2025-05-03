package com.example.kafkaproducerdemo;

import com.example.kafkaproducerdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

    private static final Logger log = LoggerFactory.getLogger(MessageProducerService.class);

    // Change KafkaTemplate value type to byte[] for protobuf
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topicName;

    public void sendMultipleMessages(int count) {
        if (count <= 0) {
            log.warn("Requested message count is zero or negative: {}", count);
            return;
        }
        log.info("Sending {} User objects (protobuf) to topic '{}'...", count, topicName);
        for (int i = 0; i < count; i++) {
            try {
                // Generate random User data
                String randomName = UserProfileGenerator.generateUsername();
                String randomBio = UserProfileGenerator.generateBio();

                // Build the User protobuf object
                User user = User.newBuilder()
                        .setName(randomName)
                        .setBio(randomBio)
                        .build();

                // Serialize User object to byte array and send
                kafkaTemplate.send(topicName, user);

                log.debug("Sent User {}/{}: Name='{}', Bio='{}'", (i + 1), count, user.getName(), user.getBio());
            } catch (Exception e) {
                log.error("Error sending User object {}/{} to Kafka topic '{}'", (i + 1), count, topicName, e);
            }
        }
        log.info("Finished sending {} User objects to topic '{}'.", count, topicName);
    }
    
}