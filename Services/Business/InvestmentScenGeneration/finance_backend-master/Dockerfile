FROM openjdk:18.0.1.1
EXPOSE 8080
ARG JAR_FILE=target/finance_backend-master-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
LABEL authors="xsl"