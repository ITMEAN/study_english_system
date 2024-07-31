FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY ../identity-service/pom.xml .
COPY ../identity-service/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
LABEL authors = "LPM"
WORKDIR /app
COPY --from=build /app/target/identity-service-0.0.1-SNAPSHOT.jar /app/identity-service-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD ["java", "-jar", "identity-service-0.0.1-SNAPSHOT.jar"]





