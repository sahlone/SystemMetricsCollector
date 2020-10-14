FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ARG JAR_NAME

ADD ${JAR_NAME} ${JAR_NAME}
ADD ./start-app.sh start-app.sh

EXPOSE 8080

ENV JAR_NAME "${JAR_NAME}"
# Dont need health check as main java process started by CMD will exit if startup fails
CMD ["sh", "start-app.sh"]
