# --- ETAPA DE CONSTRUCCIÓN (BUILD STAGE) ---
# Usa una imagen base con JDK para construir la aplicación.
# openjdk:17-slim incluye el JDK y es una variante ligera.
FROM openjdk:17-slim AS build
WORKDIR /app

# Copia el archivo pom.xml y .mvn para que Maven descargue las dependencias.
# Esto aprovecha el cache de Docker si las dependencias no cambian.
COPY pom.xml .
COPY .mvn .mvn/

# Descarga las dependencias (mvn dependency:go-offline) para que no se descarguen en cada cambio de código.
RUN mvn dependency:go-offline -B

# Copia el resto del código fuente.
COPY src src/

# Construye la aplicación Spring Boot (esto generará el JAR en target/)
RUN mvn clean install -DskipTests

# --- ETAPA FINAL (RUNNING STAGE) ---
# Usa una imagen base más ligera (solo JRE) para la aplicación final.
# eclipse-temurin:17-jre-focal es una imagen JRE muy popular y confiable.
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copia el JAR ejecutable desde la etapa de construcción a la imagen final.
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]