FROM openjdk:17-jdk

RUN apt-get update && apt-get install -y redis-server

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} application.jar
# Expose ports
EXPOSE 8080 6379

ENTRYPOINT ["java", "-jar", "application.jar"]