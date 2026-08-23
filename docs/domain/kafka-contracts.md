# Initial Kafka contracts

These are the initial known contracts for the first distributed flow. They are intentionally small and provisional; contract versioning and schema governance will be introduced when producers and consumers are implemented.

## Shared event envelope

Every event should carry the following transport metadata:

| Field | Purpose |
| --- | --- |
| `eventId` | Unique event identifier for tracing and idempotency. |
| `eventType` | Stable event name. |
| `occurredAt` | Event timestamp in ISO-8601 UTC. |
| `sourceId` | Opaque originating-process identifier when applicable. |
| `payload` | Event-specific data. |

Consumers commit Kafka offsets only after successful processing. They must be idempotent using `eventId` and, when relevant, the business `sourceId`. Retry/backoff and DLQ streams are planned, not yet part of these initial payloads.

## Topics and events

### `order.events`

**Event:** `OrderCreated`

- Kafka key: `orderId`
- Producer: Order Service
- Initial consumers: Catalog/Inventory

```json
{
  "eventId": "evt-...",
  "eventType": "OrderCreated",
  "occurredAt": "2026-08-23T12:00:00Z",
  "payload": {
    "orderId": "ord-...",
    "items": [
      { "productId": "product-...", "quantity": 1, "unitPrice": 10.00 }
    ],
    "total": 10.00
  }
}
```

### `catalog.events`

**Event:** `InventoryReservationReserved`

- Kafka key: `sourceId` (the originating order/process identifier)
- Producer: Catalog Service, Inventory module
- Initial consumers: Payment Service

```json
{
  "eventId": "evt-...",
  "eventType": "InventoryReservationReserved",
  "occurredAt": "2026-08-23T12:00:01Z",
  "sourceId": "ord-...",
  "payload": {
    "reservationId": "res-...",
    "items": [
      { "sku": "SKU-001", "quantity": 1 }
    ]
  }
}
```

Inventory belongs to Catalog in the initial deployable, so the reservation event uses `catalog.events`, not a separate `inventory.events` topic.

### `payment.events`

**Event:** `PaymentApproved`

- Kafka key: `sourceId`
- Producer: Payment Service
- Initial consumers: Order Service

```json
{
  "eventId": "evt-...",
  "eventType": "PaymentApproved",
  "occurredAt": "2026-08-23T12:00:02Z",
  "sourceId": "ord-...",
  "sourceType": "ORDER",
  "payload": {
    "paymentId": "pay-...",
    "amount": 10.00,
    "currency": "USD"
  }
}
```

Payment does not use `orderId` as a field in its domain contract. `sourceId` plus `sourceType` keeps the contract reusable for orders, invoices, subscriptions, and future sources.

## Deferred contracts

`checkout.events`, cancellation, rejection, retry, and DLQ event schemas are not defined yet because no current consumer requirement establishes them.
