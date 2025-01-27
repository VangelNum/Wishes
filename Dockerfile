FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY . /app

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
