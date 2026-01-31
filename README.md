# NDS-API

Cross-language protocol specification and SDKs for **NoieDigitalSystem (NDS)**.

NDS-API provides:
- **Protocol Buffers** definitions (canonical schema)
- **Java SDK** (Java 21) and **.NET SDK** (.NET 10) for protocol types and adapters
- **Go / TypeScript / Python / Rust / Ruby** generated protocol types (protobuf DTOs)

NDS-API does not provide:
- a Minecraft plugin runtime
- database/storage implementations
- networking/transport implementations

## Repository layout

- `spec/` — Protocol Buffers schema and protocol documentation
- `java/` — Java SDK
- `csharp/` — .NET SDK
- `Go/` — Go protobuf types (generated)
- `Cpp/` — C++ protobuf types (generated)
- `TypeScript/` — TypeScript protobuf types (generated, published to npm)
- `Python/` — Python protobuf types (generated, published to PyPI)
- `Rust/` — Rust protobuf types (generated, published to crates.io)
- `Ruby/` — Ruby protobuf types (generated, published to RubyGems)
- `docs/` — Documentation index

## Install

### Java

Use the latest published version shown on Maven Central.

**Gradle (Kotlin)**

```kotlin
repositories { mavenCentral() }
dependencies { implementation("io.github.misty4119:noiedigitalsystem-api:<VERSION>") }
```

**Maven**

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version><!-- see Maven Central --></version>
</dependency>
```

### .NET (C#)

Use the latest published version shown on NuGet.

```bash
dotnet add package Noie.Nds.Api
```

If you only need interfaces and core types:

```bash
dotnet add package Noie.Nds.Api.Abstractions
```

### TypeScript

```bash
npm i @misty4119/nds-api
```

### Python

```bash
pip install noie-nds-api
```

### Rust

```bash
cargo add noie-nds-api
```

### Ruby

```bash
gem install noie-nds-api
```

## Protocol packages

Proto packages are organized by domain under `spec/proto/nds/`.

- Unversioned packages (compatibility/stable contract): `nds.common`, `nds.identity`, `nds.asset`, `nds.event`, `nds.transaction`, `nds.context`, `nds.query`, `nds.projection`, ...
- Versioned packages (long-term coexistence): `nds.common.v1`, `nds.identity.v1`, `nds.event.v1`, `nds.ledger.v1`, `nds.sync.v1`, `nds.metadata.v1`, `nds.memory.v1`

See `spec/docs/README.md` for the protocol documentation entry point.

## Developer documentation

- General guide (EN): `DEVELOPER_GUIDE.md`
- General guide (繁體中文): `DEVELOPER_GUIDE_TW.md`
- Minecraft guide (EN): `MINECRAFT_DEVELOPER_GUIDE.md`
- Minecraft guide (繁體中文): `MINECRAFT_DEVELOPER_GUIDE_TW.md`
- Minecraft migration plan (EN): `nds_migration_next_gen_vault_replacement_plan_for_minecraft.md`

## Local verification (optional)

If you vendor the protocol and want to verify locally:

```bash
cd spec/proto
buf lint

cd ../..
.\gradlew.bat :java:build
dotnet test .\csharp\Noie.Nds.sln -c Release
```

## License

Apache License 2.0

---

Last updated: 2026-01-31

