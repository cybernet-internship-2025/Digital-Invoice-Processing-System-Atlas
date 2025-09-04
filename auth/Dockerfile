FROM maven:3.9-amazoncorretto-21-al2023 AS BUILDER
WORKDIR /app
COPY . .
#COPY src ./src
RUN mvn clean package -DskipTests -pl auth -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=BUILDER /app/auth/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]