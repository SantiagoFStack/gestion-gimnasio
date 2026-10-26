# Receta para que un servidor de internet (Render) construya y arranque la API.
# 1) Compila el proyecto con Maven  2) Lo ejecuta solo con Java, sin Maven.

FROM maven:3.9-eclipse-temurin-21 AS compilar
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q compile dependency:copy-dependencies -DoutputDirectory=target/lib

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=compilar /app/target/classes ./classes
COPY --from=compilar /app/target/lib ./lib
EXPOSE 8080
CMD ["java", "-cp", "classes:lib/*", "api.Servidor"]
