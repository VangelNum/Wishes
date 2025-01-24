FROM gradle:jdk17 AS build
COPY . /app
WORKDIR /app
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim
COPY --from=build /kaniko/0/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]