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
## Modular monolith vs. microservices

| Aspect | Modular monolith | OrderFlow microservices |
| --- | --- | --- |
| Deployment unit | One application | Independent services |
| Communication | In-process calls | Apache Kafka events for asynchronous integration |
| Initial consistency | Can use local transactions | Eventual consistency across service boundaries |
| Persistence | Monolith-owned database | Database per service |
| Scaling | Application as a whole | Per service |
| Failures | Mostly within one process | Partial and network failures must be handled explicitly |
| Operational complexity | Lower | Higher |
| Observability | Centralized in-process | Cross-service correlation, metrics, logs, and tracing required |

Neither variant is universally superior. A modular monolith would be a valid choice for a smaller or less operationally demanding product. OrderFlow chooses microservices because the project deliberately models a high-throughput, distributed order ecosystem in which Catalog/Inventory, Order, Checkout, and Payment have distinct data ownership, lifecycle pressure, and likely scaling profiles.

### Why this choice fits a high-throughput, complex ecosystem

- **Targeted scaling:** inventory reservations, order creation, and payment processing can be scaled independently when their demand differs. A single overloaded capability does not require scaling every other capability.
- **Asynchronous load absorption:** Kafka decouples producers from consumers and absorbs short traffic bursts. Consumers can process partitions with consumer groups according to their own capacity.
- **Ordering where it matters:** aggregate IDs, such as `orderId`, are Kafka keys so events for the same lifecycle remain ordered within a partition while unrelated aggregates can be processed concurrently.
- **Failure isolation:** a payment gateway outage should slow or retry Payment processing rather than make Catalog or Order unavailable. This introduces failure-handling work, but keeps the blast radius explicit and bounded.
- **Independent evolution:** Payment gateway routing and Inventory reservation logic can grow in complexity without forcing the same deployment cadence, persistence model, or scaling decisions on other services.
- **Clear operational ownership:** database-per-service and versioned event contracts reduce direct runtime coupling. They also make data ownership and recovery responsibilities visible.

Microservices are therefore selected for **selective throughput, isolation, and ecosystem complexity**, not because service decomposition by itself guarantees higher performance. The trade-off is accepted only with the required operational foundations: idempotent consumers, retries with backoff, DLQ/retry streams, health checks, correlation IDs, and observability.
