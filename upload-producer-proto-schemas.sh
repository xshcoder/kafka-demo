#!/bin/bash

# Set the path to your folder containing .proto files
PROTO_FOLDER="./producer/src/main/proto"  # Change this to your folder path
SCHEMA_REGISTRY_URL="http://localhost:8085"  # Replace with your Schema Registry URL
TOPIC="demo-topic"  # Change this to your Kafka topic subject
 
# Iterate over all .proto files in the folder
for proto_file in "$PROTO_FOLDER"/*.proto; do
    # Check if there are any .proto files
    if [ ! -f "$proto_file" ]; then
        echo "No .proto files found in $PROTO_FOLDER"
        exit 1
    fi

    # Read and escape the content of the .proto file
  proto_content=$(<"$proto_file")
  escaped_schema=$(printf '%s' "$proto_content" | sed 's/\\/\\\\/g; s/"/\\"/g; s/$/\\n/' | tr -d '\n')

  # Construct the subject name
  subject="${TOPIC}-value"

  # Build and send the curl request
  curl -X POST "${SCHEMA_REGISTRY_URL}/subjects/${subject}/versions" \
    -H "Content-Type: application/json" \
    -d "{\"schemaType\":\"PROTOBUF\",\"schema\":\"${escaped_schema}\"}"
  
  echo -e "\nDone: $proto_file"
done

echo "All .proto schemas have been uploaded to the Schema Registry."
