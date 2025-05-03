package com.example.kafkaconsumerdemo;

import com.example.kafkaconsumerdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageConsumerService {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);
    private final AtomicLong consumedMessageCount = new AtomicLong(0);

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(User user) {
        long currentCount = consumedMessageCount.incrementAndGet();
        log.info("Received User (Count: {}): Name='{}', Bio='{}'",
                 currentCount, user.getName(), user.getBio());

        // Optional: further processing here
    }

    public long getConsumedMessageCount() {
        return consumedMessageCount.get();
    }
}
