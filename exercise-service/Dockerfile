#stage 1 build jar
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY ../exercise-service/pom.xml .
COPY ../exercise-service/src ./src
RUN mvn clean package -DskipTests

#stage 2 run
FROM openjdk:17-slim
LABEL authors = "LPM"
WORKDIR /app
COPY --from=build /app/target/exercise-service-0.0.1-SNAPSHOT.jar /app/exercise-service-0.0.1-SNAPSHOT.jar
EXPOSE 8085
CMD ["java", "-jar", "exercise-service-0.0.1-SNAPSHOT.jar"]

