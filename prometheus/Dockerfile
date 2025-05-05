# Use the official Prometheus image
FROM prom/prometheus:latest

# Copy the custom Prometheus configuration file
COPY prometheus.yml /etc/prometheus/prometheus.yml

# Prometheus exposes port 9090 by default
EXPOSE 9090

# Command to run Prometheus with the custom configuration
CMD ["--config.file=/etc/prometheus/prometheus.yml"]