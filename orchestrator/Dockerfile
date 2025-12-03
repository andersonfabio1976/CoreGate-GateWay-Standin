FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=orchestrator-1.0.0-SNAPSHOT.jar
COPY target/${JAR_FILE} app.jar

# Variáveis padrão (podem ser sobrescritas no docker run)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Porta interna do serviço orchestrator
EXPOSE 8084

# Comando final (substitui CMD + ENTRYPOINT)
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
