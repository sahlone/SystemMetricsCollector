FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ARG JAR_NAME

ADD ${JAR_NAME} ${JAR_NAME}
ADD ./start-app.sh start-app.sh

EXPOSE 8080

ENV JAR_NAME "${JAR_NAME}"
HEALTHCHECK --interval=10s --timeout=3s --retries=10 CMD netstat -an | grep 9083 > /dev/null; if [ 0 != $? ]; then exit 1; fi; || exit 1
CMD ["sh", "start-app.sh"]
