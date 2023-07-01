FROM adoptopenjdk/openjdk11
USER root
HEALTHCHECK NONE
RUN mkdir -p /application
COPY target/*.jar /application/saga-service.jar
ENTRYPOINT ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=biplus /application/saga-service.jar"]
