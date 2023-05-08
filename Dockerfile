FROM bitnami/java:17-debian-11 AS builder

ENV WORK /workspace
COPY build.gradle.kts settings.gradle.kts gradlew $WORK/
COPY gradle/ $WORK/gradle
COPY ./src/ $WORK/src

WORKDIR $WORK

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix gradlew
RUN ./gradlew clean bootJar --parallel -x test --no-daemon

FROM bitnami/java:17-debian-11

MAINTAINER dnstlr2933@gmail.com

# ENV DB_URL $DB_URL
# ENV DB_USERNAME $DB_USERNAME
# ENV DB_PASSWORD $DB_PASSWORD

RUN echo "DB_URL=${DB_URL}"
RUN echo "DB_USERNAME=${DB_USERNAME}"
RUN echo "DB_PASSWORD=${DB_PASSWORD}"

ARG JAR_FILE=workspace/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar

ENTRYPOINT ["java", \
            "-jar", \
            "-Dspring.profiles.active=prod", \
            "-Dspring-boot.run.arguments=--spring.datasource.url=jdbc:mysql://${DB_URL}:3306/sprinter?serverTimezone=UTC&characterEncoding=UTF-8, --spring.datasource.username=${DB_USERNAME}, --spring.datasource.password=${DB_PASSWORD}, -Dspring-boot.run.arguments=--flyway.url=jdbc:mysql://${DB_URL}:3306/sprinter?serverTimezone=UTC&characterEncoding=UTF-8, -Dspring-boot.run.arguments=--flyway.user=${DB_USERNAME}, -Dspring-boot.run.arguments=--flyway.password=${DB_PASSWORD}", \
            "/app.jar" \
]
