FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY build/libs/*.jar wisher-1.jar
ENTRYPOINT ["java", "-jar", "wisher-1.jar"]