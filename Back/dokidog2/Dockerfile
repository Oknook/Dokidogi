# Use OpenJDK 17 Alpine base image
FROM openjdk:17-alpine
LABEL authors="SSAFY"

# Set the working directory in the Docker container
WORKDIR /src

# COPY build/libs/*.jar app.jar !나중에 gradle version 관리할때 적절하게 수정
COPY build/libs/dokidog2-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the outside
EXPOSE 8080

# Command to run the executable jar
ENTRYPOINT ["java", "-jar", "app.jar"]