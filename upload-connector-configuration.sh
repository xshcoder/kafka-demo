#!/bin/bash

CONNECT_API_URL="http://localhost:8083/connectors"

# --- Configuration for User Connector ---
USER_CONNECTOR_NAME="elasticsearch-sink-connector-users"
USER_TOPIC="demo-topic"
USER_PROTO_CLASS="com.example.kafkaproducerdemo.model.User"

USER_CONNECTOR_CONFIG='{
  "name": "'"${USER_CONNECTOR_NAME}"'",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "'"${USER_TOPIC}"'",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.protoClassName": "'"${USER_PROTO_CLASS}"'"
  }
}'

# --- Configuration for Post Connector ---
POST_CONNECTOR_NAME="elasticsearch-sink-connector-posts"
POST_TOPIC="demo-stream-topic"
POST_PROTO_CLASS="com.example.kafkaproducerdemo.model.Post"

POST_CONNECTOR_CONFIG='{
  "name": "'"${POST_CONNECTOR_NAME}"'",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "'"${POST_TOPIC}"'",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.protoClassName": "'"${POST_PROTO_CLASS}"'"
  }
}'

# --- Submit User Connector ---
echo "Submitting connector configuration for ${USER_CONNECTOR_NAME} (Topic: ${USER_TOPIC}) to ${CONNECT_API_URL}..."
curl -X POST \
     -H "Content-Type: application/json" \
     --data "${USER_CONNECTOR_CONFIG}" \
     "${CONNECT_API_URL}"
echo ""
echo "----------------------------------------"

# --- Submit Post Connector ---
echo "Submitting connector configuration for ${POST_CONNECTOR_NAME} (Topic: ${POST_TOPIC}) to ${CONNECT_API_URL}..."
curl -X POST \
     -H "Content-Type: application/json" \
     --data "${POST_CONNECTOR_CONFIG}" \
     "${CONNECT_API_URL}"
echo ""