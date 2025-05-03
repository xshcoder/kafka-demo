FROM docker.elastic.co/kibana/kibana:8.11.3

# Configure Kibana to connect to our Elasticsearch instance
ENV ELASTICSEARCH_HOSTS=http://elasticsearch:9200
ENV ELASTICSEARCH_URL=http://elasticsearch:9200

# Disable security features for development (NOT recommended for production)
ENV XPACK_SECURITY_ENABLED=false

# Expose the default Kibana port
EXPOSE 5601