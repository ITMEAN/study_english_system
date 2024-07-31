#stage 1 build jar
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY ../test-service/pom.xml .
COPY ../test-service/src ./src
RUN mvn clean package -DskipTests

#stage 2 run
FROM openjdk:17-slim
LABEL authors = "LPM"
WORKDIR /app
COPY --from=build /app/target/test-service-0.0.1-SNAPSHOT.jar /app/test-service-0.0.1-SNAPSHOT.jar
EXPOSE 8084
CMD ["java", "-jar", "test-service-0.0.1-SNAPSHOT.jar"]
