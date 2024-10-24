#!/bin/bash

echo "Iniciando proceso de despliegue de la aplicación Spring Boot..."

# Cambiar al directorio donde se clonará el repositorio
cd /home/estudiante/Desktop || exit 1

# Clonar el repositorio (si ya existe, lo eliminamos primero)
if [ -d "back-sv-inventory" ]; then
    echo "Eliminando el directorio existente de back-sv-inventory..."
    rm -rf back-sv-inventory
fi

echo "Clonando el repositorio..."
git clone https://github.com/InnovaTech-arquitectura/back-sv-inventory.git

# Cambiar al directorio del repositorio clonado
cd back-sv-inventory || exit 1

# Establecer el entorno según el argumento pasado al script
if [[ $1 == "production" ]]; then
    export ENVIRONMENT=prod
else
    export ENVIRONMENT=testing
fi

echo "Compilando el proyecto Maven..."
mvn clean install  # Agrega este comando para compilar el proyecto

echo "Deteniendo y eliminando contenedores existentes..."
docker-compose down

echo "Limpiando imágenes de Docker no utilizadas..."
docker image prune -f

# Levantar el contenedor
echo "Levantando el contenedor con docker-compose..."
docker-compose up --build -d

echo "Despliegue completado."
