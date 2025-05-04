package com.example.kafkaproducerdemo;

import com.example.kafkaproducerdemo.model.Post;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List; // Added import
import java.util.Arrays; // Added import

public class UserPostGenerator {

    private static final String[] categories = {
            "Tech", "Lifestyle", "Travel", "Food", "Gaming", "News", "Sports", "Music", "Art", "Science"
    };

    // Added a list of meaningful words
    private static final List<String> WORDS = Arrays.asList(
            "kafka", "stream", "event", "producer", "consumer", "topic", "message", "data", "realtime", "process",
            "system", "cluster", "broker", "schema", "protobuf", "java", "spring", "boot", "microservice", "api",
            "cloud", "distributed", "log", "queue", "pipeline", "analytics", "insight", "develop", "code", "build",
            "test", "deploy", "monitor", "scale", "performance", "latency", "throughput", "resilience", "fault",
            "tolerant", "architecture", "design", "pattern", "integration", "connect", "source", "sink", "transform",
            "query", "state", "window", "join", "aggregate", "exactly", "once", "delivery", "guarantee", "offset",
            "partition", "replication", "leader", "follower", "zookeeper", "kraft", "security", "authentication",
            "authorization", "encryption", "network", "storage", "memory", "cpu", "resource", "management", "config",
            "version", "update", "release", "community", "support", "documentation", "example", "tutorial", "learning",
            "journey", "challenge", "solution", "innovation", "future", "trend", "platform", "service", "application"
    );

    // Removed CHARACTERS constant
    // private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    private static final Random random = new Random();

    /**
     * Generates random content string with length between 30 and 50 characters using meaningful words.
     *
     * @return A random string made of words.
     */
    private static String generateRandomContent() {
        StringBuilder content = new StringBuilder();
        int targetMinLength = 30;
        int targetMaxLength = 50;
        boolean firstWord = true;

        while (content.length() < targetMinLength) {
            String word = WORDS.get(random.nextInt(WORDS.size()));
            if (!firstWord) {
                content.append(" ");
            }
            // Capitalize the first word
            if (firstWord) {
                word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
                firstWord = false;
            }
            content.append(word);

            // Stop if adding another word likely exceeds max length (average word length ~5 + space)
            if (content.length() > targetMaxLength - 6 && content.length() >= targetMinLength) {
                 break;
            }
        }

        // Ensure content doesn't exceed max length by trimming if necessary
        if (content.length() > targetMaxLength) {
           // Find the last space before the max length to avoid cutting words
           int lastSpace = content.lastIndexOf(" ", targetMaxLength);
           if (lastSpace != -1 && lastSpace >= targetMinLength) {
               content.setLength(lastSpace);
           } else {
               // If no suitable space found or trimming makes it too short, just trim hard
               content.setLength(targetMaxLength);
           }
        }

        content.append("."); // Add a period at the end
        return content.toString();
    }

    /**
     * Generates a random category from the predefined list.
     *
     * @return A random category string.
     */
    private static String generateRandomCategory() {
        return categories[random.nextInt(categories.length)];
    }

    /**
     * Generates a random Timestamp within the last 30 days.
     *
     * @return A random google.protobuf.Timestamp.
     */
    private static Timestamp generateRandomTimestamp() {
        Instant now = Instant.now();
        Instant thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);
        long startSeconds = thirtyDaysAgo.getEpochSecond();
        long endSeconds = now.getEpochSecond();
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
        return Timestamp.newBuilder().setSeconds(randomEpochSecond).build();
    }

    /**
     * Generates a Post object with the given username, random content, category, and creation date.
     *
     * @param username The username for the post.
     * @return A populated Post object.
     */
    public static Post generatePost(String username) {
        return Post.newBuilder()
                .setUsername(username)
                .setContent(generateRandomContent())
                .setCategory(generateRandomCategory())
                .setCreatedate(generateRandomTimestamp())
                .build();
    }
}