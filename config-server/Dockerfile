FROM adoptopenjdk/openjdk11-openj9:alpine-nightly-slim
EXPOSE 8888
ADD target/*.jar app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart","-jar","/app.jar"]
