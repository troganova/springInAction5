FROM openjdk:21-jdk
ARG JAR_FILE=ch15/ch15tacocloud/ch15-tacocloud-web/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]