# How to run 🔧

Create a .env file inside the root like this:

```text
PG_USER=admin
PG_PASSWORD=admin
PG_DB=gestao_cursos
```

Before you run the project, make sure to run:

```bash
docker compose up -d
```

So now you can start the project

```bash
mvn spring-boot:run
```

Or

```bash
mvn clean install package

java -jar target/desafio-0.0.1-SNAPSHOT.jar
```

## Docker

Run the command below to mount the image:

```bash
docker build --tag gestao_cursos .
```

## Endpoints

```text
Application: http://localhost:8080

To see metrics: http://localhost:8080/actuator

Swagger: http://localhost:8080/swagger-ui/index.html
```
