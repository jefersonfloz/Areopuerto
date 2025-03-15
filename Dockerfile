FROM maven:3.9.6-eclipse-temurin-22-jammy AS build

# Configurar el directorio de trabajo
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar la imagen de OpenJDK 21
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copiar el archivo JAR al contenedor
COPY --from=build /app/target/sistemaVuelo-0.0.1-SNAPSHOT.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]