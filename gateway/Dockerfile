FROM openjdk:17-jdk-alpine

CMD ["./gradlew", "clean", "build", "-x", "test"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} gateway-app.jar

EXPOSE ${GATEWAY_SERVER_PORT}

ENTRYPOINT ["java","-jar","/gateway-app.jar"]