# ADR 0001: Use Apache Kafka as the asynchronous message broker

- Status: Accepted
- Date: 2026-08-23

## Context

OrderFlow requires asynchronous integration between independently deployable services, while preserving service-owned data and avoiding synchronous domain coupling. The platform must also demonstrate ordering, consumer groups, retries, idempotency, and future failure handling such as DLQs.

## Decision

Use Apache Kafka as the event broker. Local development uses a single broker in KRaft mode; ZooKeeper is not used.

The current streams are `catalog.events`, `order.events`, and `payment.events`. Topics represent domain streams rather than individual status transitions. Keys must use the aggregate identifier when ordering matters, for example `orderId` for Order lifecycle events. `checkout.events` will be created only when a consumer requires it.

## Consequences

- Services communicate asynchronously without sharing database tables.
- Consumers must commit offsets only after successful processing and use consumer groups.
- Producers and consumers need idempotency, retry/backoff, and later DLQ/retry-stream designs.
- A single broker is sufficient for the local milestone but is not a production high-availability topology.
