FROM alvrme/alpine-android:android-30-jdk11 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew :web:build

FROM nginx:1.19.2

COPY --from=builder /home/gradle/src/web/build /usr/share/nginx/html

LABEL org.opencontainers.image.source https://github.com/cuhacking/atlas
