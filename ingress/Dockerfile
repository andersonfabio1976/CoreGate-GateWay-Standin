FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=ingress-1.0.0-SNAPSHOT.jar
COPY target/${JAR_FILE} app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV CONTEXT_HOST="localhost"
ENV CONTEXT_PORT=8090

EXPOSE 8082
EXPOSE 8583

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
