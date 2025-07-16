FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y redis-server

# 나머지 빌드
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
