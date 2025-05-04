# Use the official OpenJDK 17 slim image for the final image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the local machine's target directory
COPY target/kafka-producer-demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port for the application to listen on
EXPOSE 8080

# Environment variables
ENV SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=kafka:9092
ENV APP_KAFKA_TOPIC=demo-topic
ENV APP_KAFKA_STREAM_TOPIC=demo-stream-topic
ENV SPRING_KAFKA_PRODUCER_SCHEMA_REGISTRY_URL=http://schema-registry:8081

# Start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


