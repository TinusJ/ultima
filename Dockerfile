# Use the official Eclipse Temurin image for Java 21 as the base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot fat jar into the container
# Replace 'app.jar' with the actual name of your jar file if different
COPY target/*.jar app.jar

# Expose port 8080 (default for Spring Boot)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]