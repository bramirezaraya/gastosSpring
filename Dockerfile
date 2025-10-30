# Usa la imagen oficial de Eclipse Temurin con Java 21
FROM eclipse-temurin:21-jdk
 
# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app
 
# Copia los archivos de Maven al contenedor para aprovechar la cache
COPY mvnw pom.xml ./
COPY .mvn .mvn
 
# Da permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw
 
# Descarga dependencias (mejora el cacheo de capas)
RUN ./mvnw dependency:go-offline -B
 
# Copia el resto del código fuente
COPY src src
 
# Construye la aplicación sin ejecutar tests
RUN ./mvnw clean package -DskipTests
 
# Expone el puerto estándar de Spring Boot
EXPOSE 8080
 
# Ejecuta el JAR generado por Spring Boot
# (asumiendo que el jar queda en target y contiene 'SNAPSHOT' o 'app.jar')
CMD ["sh", "-c", "java -jar target/*.jar"]