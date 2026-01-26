# NDS-API

Cross-language protocol specification and SDKs for **NoieDigitalSystem (NDS)**.

- **Traditional Chinese guide**: [`DEVELOPER_GUIDE_TW.md`](DEVELOPER_GUIDE_TW.md)

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

### What this repo is / is not

- **This repo is**: protocol specs + SDK types (Identity / Asset / Event / Transaction / Result / Context).
- **This repo is not**: a server plugin, database implementation, or runtime service. (Those live in NDS implementations such as `nds-core`, `nds-bukkit`, etc.)

### Language support

| Language | Package | Status |
|---|---|---|
| Java | `io.github.misty4119:noiedigitalsystem-api` | Stable |
| C# | `Noie.Nds.Api` / `Noie.Nds.Api.Abstractions` | Stable |
| More languages | (planned) | Planned (extend `spec/` + adapters) |

## Quick start

### Java (Maven)

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version>2.0.0</version>
</dependency>
```

### Java (Gradle)

```kotlin
implementation("io.github.misty4119:noiedigitalsystem-api:2.0.0")
```

### C# (NuGet)

```bash
dotnet add package Noie.Nds.Api --version 2.0.0
```

## Protocol

Read the protocol specification at [`spec/docs/PROTOCOL.md`](spec/docs/PROTOCOL.md).

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

## Documentation

- [Documentation index](docs/README.md)
- [Protocol specification](spec/docs/PROTOCOL.md)
- [Java SDK guide](docs/JAVA_SDK.md)
- [C# SDK guide](docs/CSHARP_SDK.md)

## License

Apache License 2.0

## Contributing

See [`CONTRIBUTING.md`](CONTRIBUTING.md).

## Project metadata

- **Project inception**: 2025-12-22
- **Last updated**: 2026-01-27
