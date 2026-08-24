# payment-service

This service is independently deployable. Its Compose project starts the application image and the PostgreSQL database owned exclusively by this service.

## Ubuntu prerequisites

Use Docker Engine with the Docker Compose plugin on Ubuntu. Run these commands from a Bash-compatible terminal.

## Configuration

1. Copy .env.example to .env before starting with Docker Compose. The .env file is ignored by Git.

- src/main/resources/application.yml reads environment variables and provides local IDE defaults: PostgreSQL at localhost:5436, Kafka at localhost:29092, and Redis at localhost:6379.
- Docker Compose loads .env into the application and PostgreSQL containers. In the example file, the Docker values point to payment-service-postgres:5432, kafka:9092, and redis:6379 through orderplatform-network.

## Start

1. From ../infrastructure, run docker compose up -d to start Kafka, Redis, and orderplatform-network.
2. Build independently with gradle bootJar; the Dockerfile also builds the JAR in its Gradle build stage.
3. Run docker compose up --build -d in this directory.
4. The application is exposed at http://localhost:8084 and PostgreSQL at localhost:5436.

## Health and lifecycle

The Spring configuration exposes health and info endpoints. PostgreSQL has a Compose health check, and the application starts only after the database is healthy.

Use docker compose down to stop this service. Destructive: docker compose down -v removes only this service PostgreSQL volume and data.
