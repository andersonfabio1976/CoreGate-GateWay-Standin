FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/${JAR_FILE} app.jar
CMD ["java", "-jar", "app.jar"]


ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV CONTEXT_HOST="localhost"
ENV CONTEXT_PORT=8090

EXPOSE 8084
EXPOSE 8091

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]