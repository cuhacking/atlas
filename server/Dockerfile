FROM gradle:jdk11 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle -Pserveronly=true :server:shadowJar

FROM openjdk:11-slim-buster

COPY --from=builder /home/gradle/src/server/build/libs /usr/src/app
WORKDIR /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "server-all.jar", "data.json"]

LABEL org.opencontainers.image.source https://github.com/cuhacking/atlas
