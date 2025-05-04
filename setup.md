This is a simple demo of kafka consumer and producer.

## commands
don't run >mvn clean install as proto files shouldn't be cleaned before compile
>comsumer/mvn clean
>consumer/mvn install
>producer/mvn clean
>producer/mvn install
>docker-compose up --build -d
>docker-compose down

## URLs
-producer http://localhost:8080
-consumer http://localhost:8081
-kafka http://localhost:9092
-zookeeper http://localhost:2181
-kafka connect http://localhost:8083
-elasticsearch http://localhost:9200
-kibana http://localhost:5601
-kafka schema registry http://localhost:8085

## reload project to recognize generated protocol buffers java source files
Ctrl + Shift + P select Java:Reload Project

## upload proto schema to schema registry
curl -X POST http://localhost:8085/subjects/demo-topic-value/versions \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "syntax = \"proto3\"; package com.example.kafkaproducerdemo.model; option java_multiple_files = true; option java_package = \"com.example.kafkaproducerdemo.model\"; option java_outer_classname = \"UserProto\"; message User { string name = 1; string bio = 2; }"
  }'

curl -X POST http://localhost:8085/subjects/demo-stream-topic-value/versions \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "syntax = \"proto3\"; import \"google/protobuf/timestamp.proto\"; package com.example.kafkaproducerdemo.model; option java_multiple_files = true; option java_package = \"com.example.kafkaproducerdemo.model\"; option java_outer_classname = \"PostProto\"; message Post { string username = 1; string content = 2; string category = 3; google.protobuf.Timestamp createdate = 4; }"
  }'

or run under a bash shell
>./upload-producer-proto-schemas.sh

verify schemas are uploaded
curl http://localhost:8085/subjects/demo-topic-value/versions
curl http://localhost:8085/subjects/demo-stream-topic-value/versions
curl http://localhost:8085/subjects
curl http://localhost:8085/subjects/demo-topic-value/versions/latest
curl http://localhost:8085/subjects/demo-stream-topic-value/versions/latest

## kafka connectors
http://localhost:8083/connectors

curl -X POST -H "Content-Type: application/json" --data '{
  "name": "elasticsearch-sink-connector-users",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "topics": "demo-topic",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.protoClassName": "com.example.kafkaproducerdemo.model.User",
    "tasks.max": "1"
  }
}'

curl -X POST -H "Content-Type: application/json" --data '{
  "name": "elasticsearch-sink-connector-posts",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "connection.url": "http://elasticsearch:9200",
    "type.name": "_doc",
    "topics": "demo-topic",
    "key.ignore": "true",
    "schema.ignore": "false",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.protoClassName": "com.example.kafkaproducerdemo.model.Post",
    "tasks.max": "1"
  }
}'

http://localhost:8083/connectors

>./upload-connector-configuration.sh
