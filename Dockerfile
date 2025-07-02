# Usa una imagen base de OpenJDK (Java Development Kit) versión 17.
# La versión 'slim' es más ligera y eficiente para contenedores.
# Esto es como decir: "Mi caja se basará en un sistema que ya tiene Java 17 instalado".
FROM openjdk:17-jdk-slim

# Instala Maven dentro de la caja. Maven es la herramienta que usas para construir tu proyecto Java.
# 'apt-get update' actualiza la lista de paquetes.
# 'apt-get install -y maven' instala Maven.
# 'rm -rf /var/lib/apt/lists/*' limpia los archivos de caché para reducir el tamaño de la imagen.
# Esto es como decir: "Dentro de mi caja, también voy a instalar la herramienta 'Maven'".
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo dentro del contenedor.
# Todos los comandos posteriores se ejecutarán desde esta carpeta.
# Esto es como decir: "Dentro de la caja, vamos a trabajar en la carpeta '/app'".
WORKDIR /app

# Copia el archivo 'pom.xml' y la carpeta '.mvn' (que contiene el wrapper de Maven)
# desde tu proyecto local a la carpeta '/app' dentro del contenedor.
# Esto se hace primero para que Docker pueda cachear las dependencias de Maven.
# Si solo cambias el código Java, no tendrá que descargar las dependencias cada vez.
# Esto es como decir: "Copia la 'receta maestra' de mi proyecto y sus herramientas auxiliares".
COPY pom.xml .
COPY .mvn .mvn

# Descarga las dependencias de Maven.
# '-B' es para modo batch (no interactivo).
# Esto es como decir: "Ahora, usando Maven, descarga todos los ingredientes que mi proyecto necesita".
RUN mvn dependency:go-offline -B

# Copia el resto del código fuente de tu aplicación (la carpeta 'src')
# desde tu proyecto local a la carpeta '/app' dentro del contenedor.
# Esto es como decir: "Ahora, copia el resto de mi código fuente".
COPY src src

# Construye la aplicación Spring Boot.
# 'mvn clean install' compila tu código y crea el archivo JAR ejecutable.
# '-DskipTests' le dice a Maven que omita la ejecución de las pruebas unitarias durante la construcción,
# lo cual es común en despliegues para ahorrar tiempo.
# Esto es como decir: "Con todos los ingredientes y el código, construye la aplicación".
RUN mvn clean install -DskipTests

# Expone el puerto 8080. Esto le dice a Docker que tu aplicación escuchará en este puerto.
# Es una declaración informativa, no abre el puerto en la red externa.
# Esto es como decir: "Mi aplicación va a usar el puerto 8080 dentro de la caja".
EXPOSE 8080

# Comando para iniciar la aplicación Spring Boot cuando el contenedor se ejecute.
# 'java -jar' ejecuta el archivo JAR ejecutable que Maven creó.
# ¡MUY IMPORTANTE! El nombre del archivo JAR debe coincidir EXACTAMENTE con el que Maven crea.
# Por defecto, Maven nombra el JAR como artifactId-version.jar
# En tu caso, es 'backend-avicolalugo-0.0.1-SNAPSHOT.jar'.
# Esto es como decir: "Cuando la caja esté lista, enciende la aplicación con este comando".
ENTRYPOINT ["java", "-jar", "target/backend-avicolalugo-0.0.1-SNAPSHOT.jar"]

