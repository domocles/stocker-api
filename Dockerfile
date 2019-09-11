FROM openjdk:8-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/stocker-0.0.1-SNAPSHOT.jar stocker.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "stocker.jar"]
