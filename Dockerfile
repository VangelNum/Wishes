FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

# Этап запуска
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=builder /app/build/libs/*.jar wisher-1.jar
ENTRYPOINT ["java", "-jar", "wisher-1.jar"]
