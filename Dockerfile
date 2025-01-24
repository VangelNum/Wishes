FROM gradle:jdk17 AS build
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon --info

FROM  openjdk:17-jdk-slim
COPY --from=build /build/libs/wisher-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]