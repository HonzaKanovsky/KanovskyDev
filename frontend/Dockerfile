# Use OpenJDK as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Kotlin JAR file (change `*.jar` if necessary)
COPY build/libs/*.jar app.jar

# Expose the application port (default for Spring Boot)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
