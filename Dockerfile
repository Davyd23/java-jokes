# Use openjdk 21 for build stage
FROM openjdk:21-rc-oraclelinux8

# Set working directory for the application
WORKDIR /app

# Copy your application code
COPY build/libs/jokes-0.0.1-SNAPSHOT.jar ./libs/app.jar


# Start the application using the JAR file
ENTRYPOINT ["java", "-jar", "./libs/app.jar"]