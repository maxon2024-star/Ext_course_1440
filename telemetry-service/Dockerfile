FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY telemetry-service/build.gradle.kts telemetry-service/
COPY telemetry-service/src telemetry-service/src
RUN gradle :telemetry-service:build -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/telemetry-service/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]