FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} eureka-app.jar

EXPOSE ${EUREKA_SERVER_PORT}

ENTRYPOINT ["java","-jar","/eureka-app.jar"]