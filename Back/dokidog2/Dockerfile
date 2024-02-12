# Use OpenJDK 17 Alpine base image
FROM openjdk:17-alpine
LABEL authors="SSAFY"

# Set the working directory in the Docker container
WORKDIR /src

./gradlew build

COPY build/libs/*.jar app.jar

# Expose port 8080 to the outside
EXPOSE 8080

# Command to run the executable jar
ENTRYPOINT ["java", "-jar", "app.jar"]