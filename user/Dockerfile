FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} user-app.jar

ENTRYPOINT ["java","-jar","/user-app.jar"]