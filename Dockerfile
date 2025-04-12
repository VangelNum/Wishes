# Используем только JRE, так как сборка происходит в CI
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Копируем JAR-файл, собранный в CI, в образ.
# Предполагается, что перед запуском 'docker build' в CI,
# JAR-файл будет помещен в контекст сборки (например, в build/libs/).
# Шаг 'docker build' в CI должен выполняться ПОСЛЕ шага './gradlew bootJar'.
COPY build/libs/*.jar app.jar
# Если вы настроите CI так, чтобы JAR копировался в корень перед сборкой Docker,
# то можно использовать: COPY app.jar app.jar

# Точка входа остается прежней
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]