package com.example.kafkaconsumerdemo;

import com.example.kafkaconsumerdemo.model.Post;
import com.example.kafkaconsumerdemo.model.User;
import org.apache.kafka.common.serialization.Serde; // Added import
import org.apache.kafka.common.serialization.Serdes; // Added import
import org.apache.kafka.streams.StreamsBuilder; // Added import
import org.apache.kafka.streams.kstream.*; // Added imports for KStream, TimeWindows, Consumed, Grouped, Materialized
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Added import
import org.springframework.beans.factory.annotation.Value; // Added import
import org.springframework.context.annotation.Bean; // Added import
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration; // Added import
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageConsumerService {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);
    // Renamed counter for users
    private final AtomicLong consumedUserCount = new AtomicLong(0);
    // Added counter for total posts processed by the stream instance
    private final AtomicLong consumedPostCount = new AtomicLong(0);

    @Value("${app.kafka.topic}") // Topic for User messages
    private String userTopicName;

    @Value("${app.kafka.stream-topic}") // Topic for Post messages
    private String postTopicName;

    // Autowire the Post Serde bean defined in KafkaConsumerStreamConfig
    @Autowired
    private Serde<Post> postProtobufSerde;

    // --- Standard Kafka Listener for User messages ---
    @KafkaListener(topics = "${app.kafka.topic}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "userKafkaListenerContainerFactory") // Ensure correct factory is referenced
    public void listenUser(User user) { // Renamed method for clarity
        long currentCount = consumedUserCount.incrementAndGet();
        log.info("Received User (Count: {}): Name='{}', Bio='{}'",
                 currentCount, user.getName(), user.getBio());
        // Optional: further processing here
    }

    // --- Kafka Streams Processor for Post messages ---
    @Bean
    public KStream<String, Post> processPosts(StreamsBuilder kStreamBuilder) {
        KStream<String, Post> postStream = kStreamBuilder.stream(
                postTopicName,
                Consumed.with(Serdes.String(), postProtobufSerde) // Use String key Serde and specific Post value Serde
        );

        // Log each received post and increment the total counter
        postStream.peek((key, post) -> {
            long currentPostCount = consumedPostCount.incrementAndGet();
            log.info("Received Post (Stream Total Count: {}): User='{}', Category='{}', Content='{}...'",
                     currentPostCount, post.getUsername(), post.getCategory(), post.getContent().substring(0, Math.min(post.getContent().length(), 50)));
            // Note: This count reflects total posts processed by this stream instance, used for the controller endpoint.
        });

        // Group posts by username (key), window by 20 minutes, and count
        postStream
            .groupByKey(Grouped.with(Serdes.String(), postProtobufSerde)) // Group by username (key)
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(20))) // 20-minute tumbling window
            .count(Materialized.as("post-counts-by-user-windowed")) // Name for the state store
            .toStream() // Convert the resulting KTable back to a KStream to log results
            .foreach((windowedUsername, count) -> {
                // Log the result of the windowed aggregation
                log.info("Windowed Post Count: User='{}', Window='{}'-'{}', Count={}",
                         windowedUsername.key(), // Extract username from Windowed<String>
                         windowedUsername.window().startTime(), // Window start time
                         windowedUsername.window().endTime(),   // Window end time
                         count); // The count for this user in this window
            });

        // The stream topology is defined, Spring Kafka Streams manages its lifecycle.
        // Returning the stream is optional but can be useful for chaining or testing.
        return postStream;
    }


    // --- Methods to get counts ---

    // Renamed getter for user count
    public long getConsumedUserCount() {
        return consumedUserCount.get();
    }

    // Getter for the total posts processed by the stream instance
    public long getConsumedPostCount() {
        return consumedPostCount.get();
    }
}
