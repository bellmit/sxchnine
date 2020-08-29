FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim
VOLUME /tmp
ADD target/*.jar app.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /app.jar