#!/bin/bash

CONNECT_API_URL="http://localhost:8083/connectors"

CONNECTOR_NAME="elasticsearch-sink-connector"

CONNECTOR_CONFIG='{
  "name": "'"${CONNECTOR_NAME}"'",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "demo-topic",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.protoClassName": "com.example.kafkaproducerdemo.model.User"
  }
}'

echo "Submitting connector configuration to ${CONNECT_API_URL}..."

curl -X POST \
     -H "Content-Type: application/json" \
     --data "${CONNECTOR_CONFIG}" \
     "${CONNECT_API_URL}"

echo ""