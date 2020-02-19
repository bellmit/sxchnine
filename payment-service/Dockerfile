FROM adoptopenjdk/openjdk8-openj9:alpine-slim
VOLUME /tmp
ADD target/*.jar app.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /app.jar


