FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} order-app.jar

EXPOSE ${ORDER_SERVER_PORT}

ENTRYPOINT ["java","-jar","/order-app.jar"]