# Architecture baseline

OrderFlow is a monorepo of independently deployable Kotlin/Spring Boot services. Each service owns a PostgreSQL database and joins shared local Kafka and Redis infrastructure through Docker networking.

## Services

- Catalog: product and inventory modules in one deployable.
- Checkout: temporary purchase preparation.
- Order: commercial transaction lifecycle.
- Payment: payment, authorization, capture, refund, and gateway routing modules in one deployable.

## Messaging

Apache Kafka runs in KRaft mode. The initial domain streams are `catalog.events`, `order.events`, and `payment.events`. Use aggregate IDs as Kafka keys wherever ordering per aggregate matters. `checkout.events` is deferred until a real consumer exists.

## Ownership

Every service has an isolated PostgreSQL database and persistent volume. Cross-service joins and shared tables are prohibited. Kafka events are the preferred asynchronous integration boundary.
