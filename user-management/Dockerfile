# syntax=docker/dockerfile:1
FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /app


COPY pom.xml .
COPY user-management/pom.xml ./user-management/


RUN --mount=type=cache,target=/root/.m2/repository mvn -f user-management/pom.xml dependency:go-offline


COPY user-management/src ./user-management/src


RUN --mount=type=cache,target=/root/.m2/repository mvn -f user-management/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=builder /app/user-management/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]