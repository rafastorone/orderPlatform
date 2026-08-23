# Initial API contracts

Only one synchronous API is currently known. The first milestone keeps synchronous interaction at the ingress boundary and uses Kafka for service-to-service progression.

## Order Service

### `POST /orders`

Creates the commercial transaction and initiates the asynchronous flow.

**Request body**

```json
{
  "items": [
    {
      "productId": "product-...",
      "quantity": 1,
      "unitPrice": 10.00
    }
  ]
}
```

**Response**

The success status, response body, validation errors, and idempotency-header format are not defined yet. They will be specified with the Order API implementation.

After persistence, Order Service publishes `OrderCreated` to `order.events`, using the generated order ID as the Kafka key. Authentication and order query endpoints remain intentionally undefined.

## No other initial HTTP APIs

Catalog, Checkout, and Payment APIs are not specified yet. Inventory reservation and payment processing are initiated by Kafka events in the initial flow, not by synchronous inter-service HTTP calls.
