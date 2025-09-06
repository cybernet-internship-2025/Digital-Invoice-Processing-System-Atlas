# syntax=docker/dockerfile:1
FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /app


COPY pom.xml .
COPY notification/pom.xml ./notification/


RUN --mount=type=cache,target=/root/.m2/repository mvn -f notification/pom.xml dependency:go-offline


COPY notification/src ./notification/src


RUN --mount=type=cache,target=/root/.m2/repository mvn -f notification/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=builder /app/notification/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]