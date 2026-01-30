# v2 to v3 Bridge (Mapping Guide)

This document describes a practical mapping between unversioned protocol packages and versioned packages.
It is intended for developers building adapters, migration tooling, or compatibility layers.

## Scope

This document covers:
- identity mapping (`nds.identity` → `nds.identity.v1`)
- amount mapping (`nds.common.Decimal` / deltas → `nds.ledger.v1.Money`)
- event/transaction correlation (`nds.transaction` / `nds.event` → `nds.event.v1` envelopes)
- zero-downtime migration strategies at the protocol boundary

This document does not define:
- storage schemas
- runtime consistency guarantees beyond what the protocol expresses
- transport choice (gRPC/WebSocket/etc.)

## Identity mapping

### v2 shape

In v2, identities are represented as a typed string (e.g. `PLAYER:uuid`, `SYSTEM:system:admin`).

### v3 shape

In v3, cross-platform ownership uses `nds.identity.v1.PersonaId` and optional external bindings.

### Mapping rules

Recommended approach:

- **PersonaId** is a stable internal identifier issued by the runtime.
- A compatibility adapter MAY treat a v2 identity string as an **external binding input**, and ask the runtime to resolve a PersonaId.

Suggested binding mapping:

- `PLAYER:<uuid>` → `ExternalProvider = EXTERNAL_PROVIDER_MINECRAFT`, `external_id = <uuid>`
- `EXTERNAL:<...>` → `ExternalProvider = EXTERNAL_PROVIDER_OIDC` (or another provider based on your system), `external_id = <subject>`
- `SYSTEM:<...>` → keep as a runtime-defined system persona (implementation-defined)

## Amount mapping (Decimal ↔ Money)

### v2 shape

Economic values in v2 are string-based decimals (`nds.common.Decimal.value`).

### v3 shape

Economic values in v3 hot paths use fixed-point `nds.ledger.v1.Money`:

\[
amount = units + \frac{nanos}{10^9}
\]

### Mapping rules

- Convert v2 decimal to v3 money **exactly** (no rounding).
- If the value cannot be represented exactly at nanos precision, the adapter MUST define a policy:
  - reject (recommended), or
  - apply explicit rounding and record it in metadata/audit fields (implementation-defined)

## Event and transaction correlation

### v2 shape

Transactions and events use the unversioned packages:
- `nds.transaction.NdsTransaction`
- `nds.event.NdsEvent`

### v3 shape

Streaming uses `nds.event.v1.EventEnvelope` + `nds.sync.v1` request/response shapes.

### Mapping rules

- A compatibility layer MAY wrap a v2 transaction commit as a v3 event envelope:
  - `event_id`: server-issued opaque bytes
  - `occurred_at`: v2 occurred_at
  - `actor`: resolved PersonaId from the originating identity
  - `type`: choose a stable v3 classification (e.g. `EVENT_TYPE_LEDGER_TX_COMMITTED`)
  - `ctx`: propagate request id / idempotency key when available

## Zero-downtime migration strategies

The protocol supports coexistence. Common approaches:

- **Dual-write**: a runtime writes both v2 and v3 representations, with v3 as the long-term source.
- **Single-write + projection**: a runtime writes v3 only and exposes v2 views through projections/adapters.

For client integrations:

- Prefer subscribing to v3 streams for real-time updates.
- Use v2 APIs only when you must support legacy integrations, and keep that surface minimal.

---

Last updated: 2026-01-31

