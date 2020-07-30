FROM adoptopenjdk/openjdk8-openj9:alpine-slim
EXPOSE 8888
ENV EUREKA_SERVER = http://discovery-service:8761/eureka/
#ENV RABBITMQ_SERVER = rabbitmq-cluster.rabbitmq
ADD target/*.jar app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart","-jar","/app.jar"]
