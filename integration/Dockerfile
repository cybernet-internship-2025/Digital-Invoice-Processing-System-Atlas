# syntax=docker/dockerfile:1
FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /app


COPY pom.xml .
COPY integration/pom.xml ./integration/


RUN --mount=type=cache,target=/root/.m2/repository mvn -f integration/pom.xml dependency:go-offline


COPY integration/src ./integration/src


RUN --mount=type=cache,target=/root/.m2/repository mvn -f integration/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=builder /app/integration/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]