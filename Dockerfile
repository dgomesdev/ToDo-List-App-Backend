FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk

COPY . .

RUN apt-get install -y maven
RUN mvn clean install

FROM openjdk:17-sdk-slim

EXPOSE 8080

COPY --from=build /target/javaToDoApp-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]