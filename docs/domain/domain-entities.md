# Domain entities

## Catalog Service

Catalog contains two logical modules in one deployable: **Product** and **Inventory**. They remain separate so Inventory can be extracted later without redesigning the model.

### Product

| Field | Meaning |
| --- | --- |
| `id` | Product identifier. |
| `sku` | Sellable stock-keeping unit. |
| `name` | Product name. |
| `price` | Current catalog price. |
| `status` | Product availability status. |
| `createdAt`, `updatedAt` | Audit timestamps. |

### Inventory

**Stock**: `sku`, `availableQuantity`, `reservedQuantity`.

**Reservation**: `id`, `sourceId`, `items[]` (`sku`, `quantity`), `status`, `createdAt`, `updatedAt`.

Product describes what may be sold; Stock describes whether it can be sold now. Stock state is deliberately not merged into Product.

## Checkout Service

A **Checkout** temporarily prepares a purchase: `id`, `items[]` (`productId`, `quantity`, `unitPrice`), `total`, `status`, `createdAt`, and `updatedAt`.

Checkout retains the price used at preparation time. It does not own payment processing or the order lifecycle.

## Order Service

An **Order** is the commercial transaction: `id`, `items[]` (`productId`, `quantity`, `unitPrice`), `total`, `status`, `createdAt`, and `updatedAt`.

The core Order model must not carry payment, reservation, gateway, authorization, or other foreign-domain identifiers unless a concrete domain need appears. Integration happens through commands and events.

## Payment Service

Payment is a reusable payment platform, not an e-commerce-only service. Its entities are Payment, Authorization, Capture, and Refund; they are modules in the same deployable, not separate microservices.

### Payment

| Field | Meaning |
| --- | --- |
| `id` | Payment identifier. |
| `sourceId` | Opaque identifier of the process/entity that initiated payment. |
| `sourceType` | Type of that source, such as `ORDER`, `INVOICE`, or `SUBSCRIPTION`. |
| `amount`, `currency` | Monetary amount and currency. |
| `paymentMethod` | Requested payment method. |
| `status` | Payment lifecycle status. |

`sourceId` plus `sourceType` is intentional. Payment must not expose an `orderId` field: doing so would couple its reusable domain to Order and make other sources second-class cases. The opaque pair preserves traceability at the boundary while allowing payments for orders, invoices, subscriptions, and future products without changing the model.

### Payment lifecycle modules

- **Authorization**: `id`, `amount`, `gateway`, `status`, `createdAt`, `updatedAt`.
- **Capture**: `id`, `amount`, `status`, `createdAt`, `updatedAt`.
- **Refund**: `id`, `amount`, `status`, `createdAt`, `updatedAt`.

Gateway-specific behavior remains in infrastructure adapters behind a `PaymentGateway` port and a gateway-selection strategy. Domain entities do not contain gateway integration logic.

## Cross-domain rule

At service boundaries, prefer opaque identifiers such as `sourceId` instead of another service's entity fields. This avoids bidirectional coupling and preserves independent deployability.
