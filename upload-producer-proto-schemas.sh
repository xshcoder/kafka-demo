#!/bin/bash

# Set the path to your base folder containing topic subfolders with .proto files
PROTO_BASE_FOLDER="./producer/src/main/proto"
SCHEMA_REGISTRY_URL="http://localhost:8085" # Replace with your Schema Registry URL

echo "Starting schema upload process..."

# Check if the base folder exists
if [ ! -d "$PROTO_BASE_FOLDER" ]; then
    echo "Error: Base proto folder '$PROTO_BASE_FOLDER' not found."
    exit 1
fi

# Iterate over each subfolder (topic) in the base folder
for topic_folder in "$PROTO_BASE_FOLDER"/*/; do
    # Check if it's actually a directory
    if [ -d "$topic_folder" ]; then
        # Extract the topic name (subfolder name)
        # Remove trailing slash and get the base name
        topic_name=$(basename "$topic_folder")
        echo "Processing topic: '$topic_name' from folder '$topic_folder'"

        # Iterate over all .proto files in the current topic subfolder
        proto_files_found=false
        for proto_file in "$topic_folder"*.proto; do
            # Check if any .proto files exist in this subfolder
            if [ -f "$proto_file" ]; then
                proto_files_found=true
                echo "  Processing file: '$proto_file'"

                # Read and escape the content of the .proto file
                proto_content=$(<"$proto_file")
                # More robust escaping for JSON, handles various special characters
                escaped_schema=$(printf '%s' "$proto_content" | python -c 'import json, sys; print(json.dumps(sys.stdin.read()))' | sed 's/^"\(.*\)"$/\1/') # Use python for better JSON escaping if available, fallback sed might be needed depending on shell

                # Construct the subject name using the topic (folder) name
                subject="${topic_name}-value"
                echo "    Subject: '$subject'"

                # Build and send the curl request
                echo "    Uploading schema to ${SCHEMA_REGISTRY_URL}/subjects/${subject}/versions ..."
                curl -s -X POST "${SCHEMA_REGISTRY_URL}/subjects/${subject}/versions" \
                  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
                  -d "{\"schemaType\":\"PROTOBUF\",\"schema\":\"${escaped_schema}\"}"

                # Add a small delay if needed, e.g., sleep 1
                echo -e "\n    Done: $proto_file"
            fi
        done

        if [ "$proto_files_found" = false ]; then
            echo "  No .proto files found in '$topic_folder'"
        fi
        echo "----------------------------------------"
    fi
done

echo "All .proto schemas from subfolders have been processed."

echo "All .proto schemas have been uploaded to the Schema Registry."
