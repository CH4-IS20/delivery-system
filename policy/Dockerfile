FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} policy-app.jar

ENTRYPOINT ["java","-jar","/policy-app.jar"]