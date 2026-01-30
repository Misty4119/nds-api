# Documentation Index

This folder contains **human-facing documentation** for the NDS-API monorepo.

## Start here

- **Protocol specification**: [`../spec/docs/PROTOCOL.md`](../spec/docs/PROTOCOL.md)
- **Versioned packages (v1)**: [`../spec/docs/V3.md`](../spec/docs/V3.md)
- **Unversioned → versioned mapping**: [`../spec/docs/V3_BRIDGE.md`](../spec/docs/V3_BRIDGE.md)
- **Java SDK guide**: [`JAVA_SDK.md`](JAVA_SDK.md)
- **C# SDK guide**: [`CSHARP_SDK.md`](CSHARP_SDK.md)
- **Contributing**: [`../CONTRIBUTING.md`](../CONTRIBUTING.md)
- **Traditional Chinese developer guide**: [`../DEVELOPER_GUIDE_TW.md`](../DEVELOPER_GUIDE_TW.md)

## Repository structure

- `spec/`: Protocol Buffers definitions (+ Buf config)
- `java/`: Java SDK (published to Maven Central)
- `csharp/`: C# SDK (published to NuGet)

## Adding a new language SDK

At a high level:

1. Update/extend protocol definitions in `spec/proto/` (keep backward compatibility).
2. Generate types from proto (or implement equivalents) for the new language.
3. Implement adapter/conversion layer so the SDK matches the protocol semantics (e.g., Decimal, Result, IDs).
4. Add `docs/<LANG>_SDK.md` and a CI job to build/test/publish.

