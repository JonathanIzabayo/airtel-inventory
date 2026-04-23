# Force rebuild - April 23
# Build stage - compile the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage - use Eclipse Temurin
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/inventory-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
