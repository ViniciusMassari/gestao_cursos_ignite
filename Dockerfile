

FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY mvnw ./mvnw
COPY .mvn ./.mvn
RUN ./mvnw dependency:go-offline

COPY src/ ./src/
RUN ./mvnw package -DskipTests

FROM bellsoft/liberica-openjdk-alpine-musl:21
COPY --from=builder /app/target/desafio-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
