FROM ubuntu:22.04 AS build

RUN apt-get update && apt-get install -y openjdk-17-jdk && apt-get clean

COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-jammy

COPY --from=build /build/libs/wisher-1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]