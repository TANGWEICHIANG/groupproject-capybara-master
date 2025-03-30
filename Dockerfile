# Use the official OpenJDK image as base image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file and the application.properties file into the container at the specified location
COPY target/Group-Capybara-1.0.jar /app/Group-Capybara-1.0.jar
COPY src/main/resources/application.properties /app/application.properties

# Expose the port that your Spring Boot application uses
EXPOSE 8081

# Run the application when the container starts
CMD ["java", "-jar", "/app/Group-Capybara-1.0.jar"]
