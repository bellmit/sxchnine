FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Xshareclasses -Xquickstart -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]