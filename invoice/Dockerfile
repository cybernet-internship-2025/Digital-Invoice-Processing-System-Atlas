FROM maven:3.8.5-openjdk-17-slim AS BUILDER
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=BUILDER /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]