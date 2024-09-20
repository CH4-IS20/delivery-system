FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} company-app.jar

EXPOSE ${COMPANY_SERVER_PORT}

ENTRYPOINT ["java","-jar","/company-app.jar"]