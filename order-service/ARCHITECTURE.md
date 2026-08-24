# order-service architecture

## Package layout

- domain: isolated business modules. This layer contains domain entities and rules only.
- application/port/inbound: use-case interfaces invoked by inbound adapters.
- application/port/outbound: interfaces required by the application layer.
- application/usecase: implementations that orchestrate domain behavior through ports.
- adapter/inbound/http: HTTP controllers and request/response mapping.
- adapter/outbound/persistence: PostgreSQL/R2DBC implementations of persistence ports.
- adapter/outbound/messaging: Kafka producer and consumer adapters.
- configuration: Spring wiring and technical configuration.

## Dependency rule

Dependencies point inward: adapters depend on application ports; application depends on domain; domain depends on neither Spring nor infrastructure. No package in this service may import another service's domain model.

The current interfaces are intentionally empty placeholders. Concrete operations are added only with their documented API or Kafka contract, keeping the infrastructure milestone free of premature business behavior.
