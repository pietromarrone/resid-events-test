# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Make port 8888 available to the world outside this container
EXPOSE 8888

# Add the application's jar to the container
ADD target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]