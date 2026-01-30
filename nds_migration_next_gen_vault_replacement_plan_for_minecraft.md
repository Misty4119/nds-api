# Minecraft Migration Plan (Vault to NDS)

This document helps Minecraft server and plugin developers migrate from **Vault-style economy calls** to an **NDS protocol-driven** integration.

The goal is to keep legacy plugins working during migration, while making new integrations consistent, auditable, and cross-server friendly.

## Scope

This document covers:
- how to integrate NDS in Minecraft environments (Spigot/Paper/Folia)
- how to migrate plugin code that currently uses Vault
- how to design a compatibility adapter when you must expose Vault APIs

This document does not cover:
- database design
- runtime storage/atomicity implementation details
- policy engines or rule systems

## Terms

- **Protocol**: the portable, language-agnostic contract defined in `spec/proto/`
- **Runtime**: the implementation that persists state and applies transactions
- **Adapter**: a compatibility layer (e.g., Vault Economy) that translates legacy calls into protocol operations

## Target integration shape

```
Your plugins (new + legacy)
        │
        ▼
Compatibility adapter (optional; legacy only)
        │
        ▼
NDS protocol types (this repository)
        │
        ▼
NDS runtime implementation (authority + storage + projections)
```

## Migration principles

- **Exact economic values**: use `BigDecimal` (Java) / `decimal` (C#). Avoid floating point for balances.
- **Async-first**: do not block the server thread to wait for IO results.
- **Protocol boundary**: balance changes should be expressed as protocol-defined operations (transactions/events), not direct storage edits.
- **Auditability**: attach human/machine context using reason/metadata fields where available, without embedding secrets/PII.

## Migration checklist (practical)

1) Inventory your economy calls
- Find all `Economy#getBalance`, `withdrawPlayer`, `depositPlayer`, `has`, and any direct balance storage.

2) Replace numeric primitives
- Convert all amounts to `BigDecimal`/`decimal` at the edge.

3) Replace polling/caching with queries and results
- Query balances from the runtime when needed.
- Avoid local caching of balances unless you have a documented invalidation strategy (e.g., stream-based updates).

4) Replace “event cancellation” patterns
- Don’t rely on cancelling third-party economy events for enforcement.
- Prefer “call → result” patterns and handle failures explicitly.

5) Introduce reasons and metadata
- Use stable reason tokens (e.g., `SHOP_PURCHASE`, `QUEST_REWARD`) and attach lightweight metadata (item id, shop id, correlation id).

## Vault adapter guidance (when you must keep Vault)

Vault APIs are synchronous. If you must provide a Vault adapter:

- **Translate early**: convert Vault inputs to protocol types immediately (amounts, identity, asset ids).
- **Bound blocking**: if you must block, enforce timeouts and document that behavior.
- **Be explicit about precision**: Vault uses `double`; define rounding behavior and document potential precision loss.
- **Do not expand Vault’s surface**: expose what Vault requires; keep new capabilities in NDS-native APIs.

## Common pitfalls

- **Blocking the main thread**: calling `.get()` / `.join()` in command handlers or event callbacks will cause lag.
- **Storing balances in memory**: it will drift from the authoritative runtime state, especially on multi-server setups.
- **Using floating point for money**: introduces rounding issues and reconciliation failures.
- **Putting Bukkit objects into payloads**: payloads must be serializable primitives/collections only.

## Where to go next

- Minecraft integration guide: `MINECRAFT_DEVELOPER_GUIDE.md`
- General guide (Java + .NET): `DEVELOPER_GUIDE.md`
- Protocol docs: `spec/docs/README.md`

---

Last updated: 2026-01-31

