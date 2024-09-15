FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/core-gameplay-service.jar /app/core-gameplay-service.jar
ENTRYPOINT ["java", "-jar", "/app/core-gameplay-service.jar"]