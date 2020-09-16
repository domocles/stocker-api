FROM openjdk:8
ADD ./target/stocker-0.0.1-SNAPSHOT.jar stocker.jar
ADD ./src/main/resources/wrapper.sh /wrapper.sh
RUN bash -c 'touch /stocker.jar'
RUN bash -c 'chmod +x /wrapper.sh'
ENTRYPOINT ["/bin/bash", "/wrapper.sh"]
