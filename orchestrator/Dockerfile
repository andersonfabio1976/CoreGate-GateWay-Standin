FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/orchestrator-1.0.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV CONTEXT_HOST="localhost"
ENV CONTEXT_PORT=8090

EXPOSE 8084
EXPOSE 8091

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]