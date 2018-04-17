FROM openjdk:8-jre-slim

#Install curl
RUN apt-get update \
    && apt-get install -y chromedriver

# Add a chrome user and setup home dir.
RUN groupadd --system chrome \
    && useradd --system --create-home --gid chrome --groups audio,video chrome \
    && mkdir --parents /home/chrome/reports \
    && chown --recursive chrome:chrome /home/chrome

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS="" \
    APPLICATION_PATH_DOWNLOAD=/opt/biziapp/download \
    APPLICATION_PATH_CSV=/opt/biziapp/csv \
    APPLICATION_PATH_DOCKER_SHARED=/opt/biziapp/compartida \
    APPLICATION_DOCKER=true

RUN mkdir --parents $APPLICATION_PATH_DOWNLOAD \
    && chown chrome:chrome $APPLICATION_PATH_DOWNLOAD \
    && mkdir --parents $APPLICATION_PATH_CSV \
    && chown chrome:chrome $APPLICATION_PATH_CSV \
    && mkdir --parents $APPLICATION_PATH_DOCKER_SHARED \
    && chown chrome:chrome $APPLICATION_PATH_DOCKER_SHARED

USER chrome

# add directly the war
ADD *.war /app.war

EXPOSE 8080
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
