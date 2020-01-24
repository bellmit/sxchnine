FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim AS build
#RUN ["jlink", "--compress=2", "--module-path", "/opt/java/openjdk/jmods", "--add-modules", "java.base,javax.persistence.Persistence",  "--output", "/jlinked"]
#RUN jlink \
#    --add-modules jdk.unsupported,java.sql,java.desktop,java.naming,java.management,java.instrument,java.security.jgss \
#    --verbose \
#    --strip-debug \
#    --compress 2 \
#    --no-header-files \
#    --no-man-pages \
#    --output /jlinked


#FROM alpine:3.11.0

#COPY --from=build /jlinked /usr/local/openjdk-11
#ENV JAVA_HOME=/usr/local/openjdk-11

RUN mkdir /opt/shareclasses
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS="-Xshareclasses:cacheDir=/opt/shareclasses"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
