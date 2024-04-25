# Use an official GraalVM JDK 17 image as the base
FROM oracle/graalvm-jdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the Spring Boot app jar file into the container
COPY target/movies-api-0.0.1-SNAPSHOT.jar /app/

# Expose the port the app will run on
EXPOSE 5001

# Run the app with the GraalVM native image agent
CMD ["java", "-agentlib:native-image-agent=config-output-dir=/app/native-image-config/", "-jar", "movies-api-0.0.1-SNAPSHOT.jar"]