# Use an official Elasticsearch image as a parent image
# Make sure to use a version compatible with your needs and other components (like Kibana if used)
# Check Elastic's support matrix and Docker Hub for available versions.
# Using 8.11.3 as an example, adjust as needed.
FROM docker.elastic.co/elasticsearch/elasticsearch:8.11.3

# Set environment variables
# Running in single-node mode for development/demo purposes
ENV discovery.type=single-node
# Disable X-Pack security for easier local development (NOT recommended for production)
ENV xpack.security.enabled=false
# Set heap size (adjust based on your machine's resources)
ENV ES_JAVA_OPTS="-Xms512m -Xmx512m"

# (Optional) Add custom configuration files, plugins, or scripts here
# Example: Copy a custom elasticsearch.yml
# COPY elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml

# Example: Install a plugin
# RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-icu

# Expose the default Elasticsearch ports
EXPOSE 9200 9300

# The base image defines the default command, so no CMD or ENTRYPOINT needed unless overriding