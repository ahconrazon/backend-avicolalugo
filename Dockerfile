# --- ETAPA DE CONSTRUCCIÓN (BUILD STAGE) ---
# Usa una imagen base con JDK para construir la aplicación.
FROM openjdk:17-slim AS build
WORKDIR /app

# Instala Maven dentro del contenedor de la etapa de construcción
# 'wget' se usa para descargar el instalador de Maven
# 'tar' se usa para extraer el archivo descargado
RUN apt-get update && \
    apt-get install -y wget tar && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -P /tmp && \
    tar -xzf /tmp/apache-maven-3.9.6-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/local/bin/mvn && \
    rm /tmp/apache-maven-3.9.6-bin.tar.gz && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copia el archivo pom.xml y .mvn para que Maven descargue las dependencias.
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
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copia el JAR ejecutable desde la etapa de construcción a la imagen final.
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]