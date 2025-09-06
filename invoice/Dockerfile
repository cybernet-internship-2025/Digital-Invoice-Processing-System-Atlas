# syntax=docker/dockerfile:1
FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /app


COPY pom.xml .
COPY invoice/pom.xml ./invoice/


RUN --mount=type=cache,target=/root/.m2/repository mvn -f invoice/pom.xml dependency:go-offline


COPY invoice/src ./invoice/src


RUN --mount=type=cache,target=/root/.m2/repository mvn -f invoice/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=builder /app/invoice/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]