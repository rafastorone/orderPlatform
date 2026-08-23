# Shared infrastructure

This Compose project supplies the shared local Kafka broker and Redis instance. Start it before starting a service database Compose project.

## Ubuntu prerequisites

Install Docker Engine and the Docker Compose plugin on Ubuntu. Run the commands below from a Bash-compatible terminal. The Kafka topic script uses Bash inside its Linux container and is stored with Unix (LF) line endings.

## Start

1. Copy `.env.example` to `.env` and adjust ports if necessary.
2. Run `docker compose up -d` from this directory.
3. Verify with `docker compose ps`.

## Connections

| Component | From Docker | From the host/IDE |
| --- | --- | --- |
| Kafka | `kafka:9092` | `localhost:29092` |
| Redis | `redis:6379` | `localhost:6379` |

Kafka starts in KRaft mode without ZooKeeper. The startup job creates `catalog.events`, `order.events`, and `payment.events`; `checkout.events` is intentionally absent.

## Validation

Run `docker compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --list` to list topics. Run `docker compose exec redis redis-cli ping` to verify Redis. The `orderflow-network` Docker network is created by this project.

## Stop and reset

Use `docker compose down` to stop the shared infrastructure. **Destructive:** use `docker compose down -v` to remove Kafka and Redis volumes and all their local data.
