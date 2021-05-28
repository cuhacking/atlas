FROM alvrme/alpine-android:android-30-jdk11 AS builder

RUN apk add curl && \
    (curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash) && \
    export NVM_DIR="$HOME/.nvm" && \
    [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh" \
    && nvm install 14

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew :web:build

FROM nginx:1.19.2

COPY --from=builder /home/gradle/src/web/build /usr/share/nginx/html

LABEL org.opencontainers.image.source https://github.com/cuhacking/atlas
