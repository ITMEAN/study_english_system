#stage 1 - build jar
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY ../gateway-service/pom.xml .
COPY ../gateway-service/src ./src
RUN mvn clean package -DskipTests


#stage 2 - run
FROM openjdk:17-slim
LABEL authors = "LPM"
WORKDIR /app
COPY --from=build /app/target/gateway-service-0.0.1-SNAPSHOT.jar /app/gateway-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "gateway-service-0.0.1-SNAPSHOT.jar"]



