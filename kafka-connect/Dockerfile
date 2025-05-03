FROM confluentinc/cp-kafka-connect:7.5.1

# Install the Elasticsearch connector
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-elasticsearch:14.0.11

# Install the Protobuf converter for Kafka Connect
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-protobuf-converter:latest

# Add custom configuration if needed
COPY ./config/connect-standalone.properties /etc/kafka/connect-standalone.properties

# Expose necessary ports (if you need to expose them for the container)
EXPOSE 8083