# catalog-service

This service is independently deployable. Its Compose project starts the application image and the PostgreSQL database owned exclusively by this service.

## Ubuntu prerequisites

Use Docker Engine with the Docker Compose plugin on Ubuntu. Run these commands from a Bash-compatible terminal.

## Configuration

- src/main/resources/application.yml is used when the service runs from the IDE. It connects to PostgreSQL at localhost:5433, Kafka at localhost:29092, and Redis at localhost:6379.
- src/main/resources/application-docker.yml is the container profile. It connects through orderflow-network to catalog-service-postgres:5432, kafka:9092, and redis:6379.

## Start

1. From ../infrastructure, run docker compose up -d to start Kafka, Redis, and orderflow-network.
2. Build independently with gradle bootJar; the Dockerfile also builds the JAR in its Gradle build stage.
3. Run docker compose up --build -d in this directory.
4. The application is exposed at http://localhost:8081 and PostgreSQL at localhost:5433.

## Health and lifecycle

The Spring configuration exposes health and info endpoints. PostgreSQL has a Compose health check, and the application starts only after the database is healthy.

Use docker compose down to stop this service. Destructive: docker compose down -v removes only this service PostgreSQL volume and data.
