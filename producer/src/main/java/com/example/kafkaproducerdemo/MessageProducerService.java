package com.example.kafkaproducerdemo;

import com.example.kafkaproducerdemo.model.Post; // Added import
import com.example.kafkaproducerdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random; // Added import

@Service
public class MessageProducerService {

    private static final Logger log = LoggerFactory.getLogger(MessageProducerService.class);
    private static final Random random = new Random(); // Added random generator

    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate; // Renamed for clarity

    @Autowired
    private KafkaTemplate<String, Post> postKafkaTemplate; // Added template for Posts

    @Value("${app.kafka.topic}")
    private String userTopicName; // Renamed for clarity

    @Value("${app.kafka.stream.topic}")
    private String postTopicName; // Added stream topic name

    public void sendMultipleMessages(int count) {
        if (count <= 0) {
            log.warn("Requested message count is zero or negative: {}", count);
            return;
        }
        log.info("Sending {} User objects to topic '{}' and related Posts to topic '{}'...", count, userTopicName, postTopicName);
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

                // Send User object
                userKafkaTemplate.send(userTopicName, user.getName(), user); // Use username as key
                log.info("Sent User {}/{}: Key='{}', Name='{}', Bio='{}' to topic '{}'", (i + 1), count, user.getName(), user.getName(), user.getBio(), userTopicName);

                // Send related posts for this user
                sendPostsForUser(user.getName());

            } catch (Exception e) {
                log.error("Error sending User object {}/{} or related Posts to Kafka", (i + 1), count, e);
            }
        }
        log.info("Finished sending {} User objects and related Posts.", count);
    }

    /**
     * Generates and sends a random number of Post messages (1 to 5) for a given user.
     * @param username The username to associate the posts with.
     */
    private void sendPostsForUser(String username) {
        int numberOfPosts = 1 + random.nextInt(5); // Generate 1 to 5 posts
        log.debug("Generating {} posts for user '{}' to send to topic '{}'", numberOfPosts, username, postTopicName);
        for (int j = 0; j < numberOfPosts; j++) {
            try {
                Post post = UserPostGenerator.generatePost(username);
                // Send Post object using the postKafkaTemplate
                // Using username as key to potentially partition posts by user
                postKafkaTemplate.send(postTopicName, username, post);
                log.info("Sent Post {}/{} for user '{}': Category='{}', Content='{}...'", (j + 1), numberOfPosts, username, post.getCategory(), post.getContent().substring(0, Math.min(post.getContent().length(), 20)));
            } catch (Exception e) {
                log.error("Error sending Post {}/{} for user '{}' to Kafka topic '{}'", (j + 1), numberOfPosts, username, postTopicName, e);
            }
        }
    }
}