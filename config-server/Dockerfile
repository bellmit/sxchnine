FROM adoptopenjdk/openjdk8-openj9:alpine-slim
EXPOSE 8888
ADD target/*.jar app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart","-jar","/app.jar"]
