FROM adoptopenjdk/openjdk8-openj9:alpine-slim
EXPOSE 8761
#ENV RABBITMQ_SERVER = rabbitmq-cluster.rabbitmq
ADD target/*.jar app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart","-jar","/app.jar"]
