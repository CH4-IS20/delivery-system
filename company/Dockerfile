FROM openjdk:17-jdk-alpine

CMD ["./gradlew", "clean", "build", "-x", "test"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} company-app.jar

EXPOSE ${COMPANY_SERVER_PORT}

ENTRYPOINT ["java","-jar","/company-app.jar"]