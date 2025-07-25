# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Create app folder
WORKDIR /app

# Copy mvnw with execute permission and other files
COPY --chmod=755 mvnw mvnw
COPY . .

# Build the Spring Boot project
RUN ./mvnw clean package -DskipTests

# App runs on port 8080
EXPOSE 8080

# Run the built JAR (Exact name used ✅)
CMD ["java", "-jar", "target/expense-tracker-0.0.1-SNAPSHOT.jar"]
