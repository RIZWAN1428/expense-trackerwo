FROM eclipse-temurin:17-jdk

WORKDIR /app

# 👇 Make sure mvnw copied with execute permission
COPY --chmod=755 . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/expense-tracker-0.0.1-SNAPSHOT.jar"]
