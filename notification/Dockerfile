FROM maven:3.9.6-eclipse-temurin-21 AS BUILDER
WORKDIR /app
COPY . .
#COPY src ./src
RUN mvn clean package -DskipTests -pl notification -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=BUILDER /app/notification/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]