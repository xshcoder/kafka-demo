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

# Start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


