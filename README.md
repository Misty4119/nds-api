# NDS-API

Cross-language protocol specification and SDKs for **NoieDigitalSystem (NDS)** — the Next-Generation Economy Protocol.

[![Build](https://github.com/Misty4119/nds-api/actions/workflows/build.yml/badge.svg)](https://github.com/Misty4119/nds-api/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.misty4119/noiedigitalsystem-api)](https://central.sonatype.com/artifact/io.github.misty4119/noiedigitalsystem-api)
[![NuGet](https://img.shields.io/nuget/v/Noie.Nds.Api)](https://www.nuget.org/packages/Noie.Nds.Api)

## Overview

**NDS-API** is the protocol layer (types + contracts) used by NDS implementations.  
It is designed to be **language-agnostic**, with Protocol Buffers as the **single source of truth**.

This repository is a monorepo containing:

- **`spec/`**: Protocol Buffers definitions (single source of truth)
- **`java/`**: Java SDK (Java 21)
- **`csharp/`**: C# SDK (.NET 10)
- **`docs/`**: Documentation

### What This Repository Is / Is Not

- **This repo is**: protocol specs + SDK types (Identity / Asset / Event / Transaction / Result / Context).
- **This repo is not**: a server plugin, database implementation, or runtime service. (Those live in NDS implementations such as `nds-core`, `nds-bukkit`, etc.)

### Language Support

| Language | Package | Status |
|----------|---------|--------|
| Java | `io.github.misty4119:noiedigitalsystem-api` | ✅ Stable |
| C# | `Noie.Nds.Api` / `Noie.Nds.Api.Abstractions` | ✅ Stable |
| More languages | (planned) | Planned (extend `spec/` + adapters) |

---

## Quick Start

### Java (Maven)

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version>2.1.0</version>
</dependency>
```

### Java (Gradle)

```kotlin
implementation("io.github.misty4119:noiedigitalsystem-api:2.1.0")
```

### C# (NuGet)

```bash
dotnet add package Noie.Nds.Api --version 2.1.0
```

---

## Documentation

### Developer Guides

| Guide | Language | Description |
|-------|----------|-------------|
| [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) | English | General developer guide (Java + C#) |
| [DEVELOPER_GUIDE_TW.md](DEVELOPER_GUIDE_TW.md) | 繁體中文 | General developer guide (Java + C#) |
| [MINECRAFT_DEVELOPER_GUIDE.md](MINECRAFT_DEVELOPER_GUIDE.md) | English | Minecraft plugin developer guide |
| [MINECRAFT_DEVELOPER_GUIDE_TW.md](MINECRAFT_DEVELOPER_GUIDE_TW.md) | 繁體中文 | Minecraft plugin developer guide |

### Additional Resources

- [Documentation Index](docs/README.md)
- [Protocol Specification](spec/docs/README.md)
- [Migration Guide](nds_migration_next_gen_vault_replacement_plan_for_minecraft.md)
- [AI Development Context](AGENTS.md)

---

## Protocol

Read the protocol specification at [`spec/docs/README.md`](spec/docs/README.md).

---

## Build

### Prerequisites

- Java 21+
- .NET SDK 10.0+
- Buf CLI 1.64+

### Commands

```bash
# Proto lint
buf lint spec/proto

# Java
./gradlew :java:build

# C#
dotnet build csharp/

# All tests
./gradlew :java:test && dotnet test csharp/
```

---

## Architecture

```
Your Application
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← Protocol Layer (this repository)
│   (Interfaces Only)  │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← Implementation (nds-core, nds-bukkit, etc.)
│   (Event Store,     │
│    Projections)     │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(Event Store)  (Sync / Cache)
```

---

## Core Principles

1. **Protocol First** — API is a protocol, not a tool, not an implementation
2. **Event Is The Source Of Truth** — State can only be obtained through event projection
3. **Async-First** — All API methods are asynchronous
4. **BigDecimal/decimal Only** — No floating-point numbers for economic values
5. **Result-Oriented** — Business failures expressed via `NdsResult`, not exceptions

---

## License

Apache License 2.0

---

## Contributing

See [`CONTRIBUTING.md`](CONTRIBUTING.md).

---

## Project Metadata

- **Project inception**: 2025-12-22
- **Last updated**: 2026-01-27
- **Version**: 2.1.0
- **Status**: ✅ Stable
