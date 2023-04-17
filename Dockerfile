FROM arm64v8/eclipse-temurin:17.0.6_10-jdk-jammy AS builder

ENV WORK /workspace
COPY build.gradle.kts settings.gradle.kts gradlew $WORK/
COPY gradle/ $WORK/gradle
COPY ./src/ $WORK/src

WORKDIR $WORK

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix gradlew
RUN ./gradlew clean bootJar --parallel -x test --no-daemon

FROM arm64v8/eclipse-temurin:17.0.6_10-jdk-jammy

MAINTAINER dnstlr2933@gmail.com

ENV SPRING_DATASOURCE_URL $SPRING_DATASOURCE_URL
ENV SPRING_DATASOURCE_USERNAME $SPRING_DATASOURCE_USERNAME
ENV SPRING_DATASOURCE_PASSWORD $SPRING_DATASOURCE_PASSWORD

ARG JAR_FILE=workspace/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar

ENTRYPOINT [
    "java",
    "-jar",
    "-Dspring.profiles.active=prod",
    "-Dspring-boot.run.arguments=--spring.datasource.url=${SPRING_DATASOURCE_URL}, --spring.datasource.username=${SPRING_DATASOURCE_USERNAME}, --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}, --spring.flyway.url=${SPRING_DATASOURCE_URL}, --spring.flyway.user=${SPRING_DATASOURCE_USERNAME}, --spring.flyway.password=${SPRING_DATASOURCE_PASSWORD}",
    "/app.jar"
]
