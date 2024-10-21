# Primera etapa: Construcción del JAR usando Maven
FROM maven:3.8.5-openjdk-17-slim AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y las dependencias para aprovechar el caché de Docker
COPY pom.xml .

# Descargar las dependencias del proyecto (esto aprovecha el caché si el pom.xml no ha cambiado)
RUN mvn dependency:go-offline

# Copiar todo el contenido del proyecto
COPY . .

# Construir el proyecto, lo que genera el JAR en la carpeta target
RUN mvn clean install

# Segunda etapa: Usar una imagen más ligera solo con Java para correr la aplicación
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo para la ejecución de la app
WORKDIR /app

# Copiar el JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usa la aplicación
EXPOSE 8090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
