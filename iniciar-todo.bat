@echo off

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd eureka
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > null

echo Iniciando API Gateway...
cd ../gateway
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio jugador-progreso...
cd ../jugador-progreso
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Mansion_Electric_Boogaloo-main...
cd ../Mansion_Electric_Boogaloo-main
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Personajes...
cd ../personajes
start cmd /k "mvnw spring-boot:run"

echo Ecosistema lanzado. Dashboard disponible en http://localhost:8761