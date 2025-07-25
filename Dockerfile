# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Create app folder
WORKDIR /app

# Copy all files
COPY . .

# Build the Spring Boot project
RUN ./mvnw clean package -DskipTests

# App runs on port 8080
EXPOSE 8080

# Run the built JAR
CMD ["java", "-jar", "target/*.jar"]
