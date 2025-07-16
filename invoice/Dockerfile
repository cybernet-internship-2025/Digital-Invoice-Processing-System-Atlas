FROM maven:3.8.5-openjdk-17-slim AS BUILDER
WORKDIR /app
COPY . .
#COPY src ./src
RUN mvn clean package -DskipTests -pl invoice -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=BUILDER /app/invoice/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]