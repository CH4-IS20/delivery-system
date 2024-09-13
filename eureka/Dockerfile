FROM openjdk:17-jdk-alpine

CMD ["./gradlew", "clean", "build", "-x", "test"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} eureka-app.jar

ENTRYPOINT ["java","-jar","/eureka-app.jar"]