FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim
EXPOSE 8080
COPY target/*SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler -XX:+UseG1GC","-jar","/app.jar"]
