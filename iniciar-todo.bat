@echo off

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd eureka
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > null

echo Iniciando API Gateway...
cd ../gateway
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Jugador-progreso...
cd ../jugador-progreso
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Habitaciones...
cd ../habitaciones
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Personajes...
cd ../personajes
start cmd /k "mvnw spring-boot:run"

echo Ecosistema lanzado. Dashboard disponible en http://localhost:8761