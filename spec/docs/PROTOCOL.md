# NDS Protocol (v2.0)

NDS-API is a **protocol layer** for NoieDigitalSystem (NDS).  
This repository ships:

- **Protocol Buffers definitions** (single source of truth)
- **Language SDKs** (Java / C# today, more languages planned)

The goal is to provide **one consistent contract** across languages and runtimes (Minecraft, services, tooling, etc.).

## Single source of truth

All cross-language contracts MUST originate from `spec/proto/`.  
SDKs SHOULD match the protocol semantics, even when the underlying language types differ.

## Protocol modules (proto packages)

`spec/proto/nds/*` is structured by domain:

- `nds.common`: `Decimal`, `NdsResult`, `NdsError`, standard `ErrorCode`
- `nds.identity`: `NdsIdentity`, `IdentityType`
- `nds.asset`: `AssetId`, `AssetScope`, `NdsAsset`
- `nds.event`: event base types and payload
- `nds.transaction`: transaction event (asset delta semantics) + consistency model
- `nds.context`: tracing / correlation context
- `nds.query`, `nds.projection`: query/projection contracts (for runtime implementations)

## Core invariants (non-negotiable)

- **Protocol first**: specs define behavior; implementations follow.
- **Event is the source of truth**: state is derived from events + projections (event sourcing).
- **Async-first**: network/runtime APIs should be non-blocking. Business failures are results, not exceptions.
- **Deterministic numeric**: use exact decimal semantics (no floating point for economic values).
- **Serializable & replayable**: events and payload must remain replayable across time.

## Data type mapping (by design)

`nds.common.Decimal` encodes a high precision decimal as a **string** to preserve exactness across languages.

| Protocol | Java SDK | C# SDK | Notes |
|---|---|---|---|
| `nds.common.Decimal` | `java.math.BigDecimal` | `decimal` | Proto stores string; SDK adapters convert |
| `nds.common.NdsResult` | `NdsResult<T>` | `NdsResult<T>` | Success/failure envelope |
| `nds.common.NdsError` | `NdsError` | `NdsError` | Machine code + human message |

## Canonical string formats

SDKs accept more than one format for convenience, but **the protocol recommends canonical formats** for interoperability.

### AssetId

- **Canonical**: `scope:name`
- **Examples**: `player:coins`, `server:world_boss_hp`, `global:total_circulation`
- **Naming rule**: `name` SHOULD be lowercase letters + underscores only.

### Identity

- **Canonical**: `type:id`
- **Examples**:
  - `PLAYER:550e8400-e29b-41d4-a716-446655440000`
  - `SYSTEM:system:admin`
  - `AI:ai:gpt-4`
  - `EXTERNAL:external:payment:stripe`

Notes:

- SDKs MAY default to `PLAYER` when the `type:` prefix is omitted, but callers SHOULD include the prefix in cross-system integrations.
- The `type` token SHOULD be case-insensitive in parsers; canonical form is uppercase in docs/examples.

## Versioning & compatibility

- This repo uses **semantic versioning**: `MAJOR.MINOR.PATCH`.
- **Backward compatibility** for proto is required within the same MAJOR:
  - Do not renumber fields.
  - Prefer adding new fields as `optional` where appropriate.
  - Avoid changing field meaning/units.
- SDKs MUST stay protocol-compatible for a given release tag.

## Tooling (Buf)

Proto lint should pass:

```bash
buf lint spec/proto
```

Code generation is configured via:

- `spec/proto/buf.yaml`
- `spec/buf.gen.yaml`

## Contributing changes to the protocol

1. Update `spec/proto/**` first.
2. Regenerate / update SDKs (Java + C#) to match the spec.
3. Add/adjust tests in each language SDK.
4. Update documentation links if new modules are added.

# NDS Protocol Specification

> Version: **2.0.0**  
> Last updated: **2026-01-26**  
> Status: **Draft**

---

## 1. Overview

The **NDS (NoieDigitalSystem) Protocol** is a cross-language specification for economic state, expressed using **Protocol Buffers (proto3)**.

### 1.1 Principles

- **Spec-first**: `.proto` is the single source of truth
- **Cross-language consistency**: SDKs must behave consistently across languages
- **Event sourcing**: state changes are represented as immutable events
- **Precision**: decimals are represented as strings to avoid precision loss

### 1.2 Monorepo layout

```
nds-api/
├── spec/     # L0: Protocol (single source of truth)
├── java/     # L1: Java SDK
└── csharp/   # L1: C# SDK
```

---

## 2. Proto files

All `.proto` files live under:

```
spec/proto/nds/
```

Key files:

- `common.proto` (Decimal, result/error envelopes)
- `identity.proto`
- `asset.proto`
- `context.proto`
- `event.proto`
- `transaction.proto`
- `projection.proto`
- `query.proto`

---

## 3. Core types (high-level)

### 3.1 Decimal

Proto does not have a native `BigDecimal` type. NDS uses a **string-based decimal** representation.

### 3.2 Identity

Identities represent actors (player/system/AI/external).

### 3.3 Asset

Assets are addressed by `(scope, name)` and are used by events/transactions.

---

## 4. Events

Events are immutable, serializable records of state changes.

### Rules

1. Events must be immutable
2. Events must be replayable (append-only history requirement)
3. Payload must remain platform-agnostic (no game/server objects)

---

## 5. Transactions

Transactions are events with:

- `asset`
- `delta` (decimal)
- `consistency` mode
- optional `source` / `target`
- optional `reason`
- `status`

---

## 6. Backward compatibility

- Do not remove existing fields
- Do not change field types or numbers
- Add new fields as optional (or with defaults)
- Do not delete enum values (deprecate instead)

---

## 7. Tooling (Buf)

### 7.1 Lint

```bash
buf lint spec/proto
```

### 7.2 Generate

```bash
buf generate spec/proto
```

### 7.3 Breaking change detection

```bash
buf breaking spec/proto --against ".git#branch=main"
```

---

> This document is a high-level overview. The authoritative contract is the `.proto` files under `spec/proto/`.
