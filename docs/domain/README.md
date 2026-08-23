# Domain

The domain model is split by deployable service. A service owns its data and lifecycle; it must not join or directly share tables with another service.

- [Domain entities](domain-entities.md): initial entities and boundary decisions.
- [Initial Kafka contracts](kafka-contracts.md): initial event streams, keys, payloads, and consumers.
- [Initial API contracts](api-contracts.md): the currently known synchronous API surface.
