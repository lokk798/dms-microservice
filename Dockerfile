# 1. Base on Java 17 Alpine for minimal size
FROM eclipse-temurin:17-jdk-alpine

# 2. Set working directory
WORKDIR /app

# 3. Copy the Gradle-built fat JAR into the image
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. Expose the port your service listens on
#    (change 8081/8082/8083 per service below)
EXPOSE 8082

# 5. Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]