# Use an official Confluent Schema Registry image
FROM confluentinc/cp-schema-registry:7.5.1

# Expose the default Schema Registry port
EXPOSE 8081

# Environment variables needed by Schema Registry
# Point to the Kafka brokers (using the service name from docker-compose)
ENV SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=PLAINTEXT://kafka:9092
# Define the listeners for Schema Registry itself
ENV SCHEMA_REGISTRY_HOST_NAME=schema-registry
ENV SCHEMA_REGISTRY_LISTENERS=http://0.0.0.0:8081