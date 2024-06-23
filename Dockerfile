FROM amazoncorretto:17-alpine-jdk
WORKDIR /opt
ENV PORT 8080
COPY target/*.jar /opt/spring-security.jar
ENTRYPOINT ["java","-jar","spring-security.jar"]