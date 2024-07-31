FROM   openjdk:17-oracle
LABEL authors = "LY PHI MINH"
WORKDIR /app
COPY ../discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar /app/discovery-service-0.0.1-SNAPSHOT.jar
EXPOSE 8761
CMD ["java", "-jar", "discovery-service-0.0.1-SNAPSHOT.jar"]

