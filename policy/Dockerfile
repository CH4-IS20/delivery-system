FROM openjdk:17-jdk-alpine

CMD ["./gradlew", "clean", "build", "-x", "test"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} policy-app.jar

ENTRYPOINT ["java","-jar","/policy-app.jar"]