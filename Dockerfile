FROM gradle:jdk17 AS build
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon --info

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]