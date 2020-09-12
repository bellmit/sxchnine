FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim as builder
WORKDIR application
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim
WORKDIR application
RUN mkdir /opt/shareclasses
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
ENV JAVA_OPTS="-Xshareclasses:cacheDir=/opt/shareclasses"
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]