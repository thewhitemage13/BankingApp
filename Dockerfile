FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 7070

COPY target/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
