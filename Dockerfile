# Use an official Maven image with OpenJDK 17 as a parent image
FROM maven:3.8.3-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app
COPY . .
RUN mvn clean package
RUN mv target/IMS-Saty-0.0.1-SNAPSHOT.jar target/ims-saty.jar

FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the renamed JAR file from the builder image
COPY --from=builder /app/target/ims-saty.jar app.jar

# Expose port 8999 to the outside world
EXPOSE 8999

# Define the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
