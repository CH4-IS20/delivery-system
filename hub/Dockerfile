FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} hub-app.jar

EXPOSE ${HUB_SERVER_PORT}

ENTRYPOINT ["java","-jar","/hub-app.jar"]