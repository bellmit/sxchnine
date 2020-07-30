FROM adoptopenjdk/openjdk8-openj9:alpine-slim
EXPOSE 9191
ENV CONFIG_SERVER=http://config-server:8888
ENV EUREKA_SERVER=http://discovery-service:8761/eureka
ENV ISSUER_URI=http://keycloak-keycloak.192.168.99.101.nip.io/auth/realms/api-gateway
ENV JWK_SET_URI=http://keycloak-keycloak.192.168.99.101.nip.io/auth/realms/api-gateway/protocol/openid-connect/certs
ADD target/*.jar app.jar
ENTRYPOINT ["java","-Xshareclasses -Xquickstart","-jar","/app.jar"]
