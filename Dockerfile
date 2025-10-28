# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen oficial de Eclipse Temurin con Java 17 (JDK) y Maven
FROM eclipse-temurin:17-jdk AS build

# Establecemos un directorio de trabajo
WORKDIR /app

# Copiamos solo los archivos necesarios para descargar dependencias
# Esto aprovecha el caché de Docker. Si el pom.xml no cambia, no
# vuelve a descargar todas las dependencias.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# ---- AÑADE ESTA LÍNEA ----
# Damos permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Ahora sí, descargamos las dependencias
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y creamos el .jar. Saltamos los tests.
RUN ./mvnw clean package -DskipTests

# --- ETAPA 2: EXTRACCIÓN DE CAPAS (OPTIMIZACIÓN DE SPRING) ---
# Extraemos el .jar en capas.
FROM build AS extract
WORKDIR /app
RUN java -Djarmode=layertools -jar target/*.jar extract


# --- ETAPA 3: IMAGEN FINAL (PRODUCCIÓN) ---
# Usamos una imagen "alpine", que es súper pequeña y segura.
# Solo incluye el Java Runtime 17 (JRE), no el JDK completo.
FROM eclipse-temurin:17-jre-alpine AS final

WORKDIR /app

# Exponemos el puerto 8080 (el default de Spring Boot)
EXPOSE 8080

# Copiamos las capas extraídas en el orden correcto (de menos a más cambiante)
# Esto hace que las futuras actualizaciones sean rapidísimas.
COPY --from=extract /app/dependencies/ ./
COPY --from=extract /app/spring-boot-loader/ ./
COPY --from=extract /app/snapshot-dependencies/ ./
COPY --from=extract /app/application/ ./

# El comando final para ejecutar la app usando el lanzador de Spring
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
