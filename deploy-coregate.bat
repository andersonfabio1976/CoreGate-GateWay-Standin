@echo off
setlocal

echo ================================
echo BUILDING INGRESS
echo ================================

cd ingress
mvn clean package -DskipTests
docker stop coregate-ingress
docker rm coregate-ingress
docker build -t coregate-ingress:latest .
docker run -d --name coregate-ingress -p 8082:8080 coregate-ingress:latest

cd ..\context
echo ================================
echo BUILDING CONTEXT
echo ================================

mvn clean package -DskipTests
docker stop coregate-context
docker rm coregate-context
docker build -t coregate-context:latest .
docker run -d --name coregate-context -p 8083:8080 coregate-context:latest

cd ..\orchestrator
echo ================================
echo BUILDING ORCHESTRATOR
echo ================================

mvn clean package -DskipTests
docker stop coregate-orchestrator
docker rm coregate-orchestrator
docker build -t coregate-orchestrator:latest .
docker run -d --name coregate-orchestrator -p 8084:8080 coregate-orchestrator:latest

cd ..\finalizer
echo ================================
echo BUILDING FINALIZER
echo ================================

mvn clean package -DskipTests
docker stop coregate-finalizer
docker rm coregate-finalizer
docker build -t coregate-finalizer:latest .
docker run -d --name coregate-finalizer -p 8085:8080 coregate-finalizer:latest

cd ..\mockemissor
echo ================================
echo BUILDING MOCK EMISSOR
echo ================================

mvn clean package -DskipTests
docker stop coregate-mockemissor
docker rm coregate-mockemissor
docker build -t coregate-mockemissor:latest .
docker run -d --name coregate-mockemissor -p 8086:8080 coregate-mockemissor:latest

cd ..
echo ================================
echo TODOS OS MÓDULOS INICIADOS
echo ================================
