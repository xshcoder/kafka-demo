package com.example.kafkaconsumerdemo;

import com.example.kafkaconsumerdemo.model.Post;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@EnableKafkaStreams // Enable Kafka Streams processing here
public class KafkaConsumerStreamConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.streams.application-id}")
    private String streamsApplicationId;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, streamsApplicationId);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // Configure Protobuf Serde for values by default if needed, or specify per stream
        // props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, KafkaProtobufSerde.class.getName()); // Be careful with default value serde
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl); // Add schema registry for streams Serdes
        props.put(COMMIT_INTERVAL_MS_CONFIG, 1000); // Example: Commit offsets every second
        // Add other stream configs as needed (e.g., state dir, threads)
        // props.put(STATE_DIR_CONFIG, "/tmp/kafka-streams"); // Example state directory

        return new KafkaStreamsConfiguration(props);
    }

    // Define Serdes needed for Streams (String is default key, Post is value)
    @Bean
    public Serde<Post> postProtobufSerde() {
        final Serde<Post> serde = new KafkaProtobufSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put(KafkaProtobufDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        config.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, Post.class.getName());
        serde.configure(config, false); // false for value serde
        return serde;
    }
}