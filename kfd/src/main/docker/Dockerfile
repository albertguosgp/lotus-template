FROM store/oracle/serverjre:8
MAINTAINER psaleavetest@gmail.com
# Define container mount point, the /data will be automatically mapped to a host path which
# is created and determined by Docker. Docker inspect can see that detail information.
VOLUME /data

ADD PROJECT_NAME.jar /data/app.jar
RUN sh -c 'touch /data/app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /data/app.jar" ]
EXPOSE 9090