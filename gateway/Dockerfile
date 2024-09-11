FROM openjdk:17-jdk-alpine

CMD ["./gradlew", "clean", "build", "-x", "test"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} gateway-app.jar

ENTRYPOINT ["java","-jar","/gateway-app.jar"]