# Use OpenJDK 17 with Alpine Linux (fast/lightweight)
FROM openjdk:17-jdk-alpine

# Copy the application JAR
COPY target/*.jar app.jar

# If using "logs" directory for logging, create it
RUN mkdir -p /logs

# Set application jar as ENTRYPOINT
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expose your application port
EXPOSE 8081
