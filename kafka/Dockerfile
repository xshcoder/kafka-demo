# Assuming you are using a base image like confluentinc/cp-kafka
FROM confluentinc/cp-kafka:latest

EXPOSE 9092 29092 7071

ENV KAFKA_BROKER_ID=1
ENV KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
ENV KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
ENV KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT
ENV KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT
ENV KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1

# Switch to root to create directory
USER root

# Create a directory for the agent and copy the JAR
RUN mkdir -p /opt/prometheus && chown -R appuser:appuser /opt/prometheus

# Switch back to the default user (adjust if the user is different, e.g., 'kafka')
USER appuser

COPY --chown=appuser:appuser jmx_prometheus_javaagent-0.17.0.jar /opt/prometheus/jmx_prometheus_javaagent.jar
COPY --chown=appuser:appuser kafka-broker.yml /opt/prometheus/kafka-broker.yml
# Optional: Copy a JMX exporter config file if you have one
# COPY kafka-broker.yml /opt/prometheus/kafka-broker.yml