# NDS-API - Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/io.github.misty4119/noiedigitalsystem-api.svg)](https://search.maven.org/artifact/io.github.misty4119/noiedigitalsystem-api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on Java.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

Use the latest published version shown on Maven Central.

### Gradle (Kotlin)

```kotlin
repositories { mavenCentral() }
dependencies { implementation("io.github.misty4119:noiedigitalsystem-api:<VERSION>") }
```

### Maven

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version><!-- see Maven Central --></version>
</dependency>
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Governance & audit hooks**: Policy (`noie.linmimeng.noiedigitalsystem.api.policy`) and Audit/Rationale (`noie.linmimeng.noiedigitalsystem.api.audit`)
- **Deterministic numerics**: economic values use `BigDecimal` (no floating point)
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Identity

```java
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;

// Create player identity
var player = NdsIdentity.of("550e8400-e29b-41d4-a716-446655440000", IdentityType.PLAYER);

// Parse from string
var identity = NdsIdentity.fromString("PLAYER:550e8400-e29b-41d4-a716-446655440000");
```

### Asset

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

// Create asset IDs
var coins = AssetId.of(AssetScope.PLAYER, "coins");
var bossHp = AssetId.of(AssetScope.SERVER, "world_boss_hp");

// Parse from string
var asset = AssetId.fromString("player:gold");
```

### Result Pattern

```java
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;

var success = NdsResult.success(100);
var failure = NdsResult.<Integer>failure("INSUFFICIENT_BALANCE", "Not enough coins");

var result = success
    .map(x -> x * 2)
    .onSuccess(x -> System.out.println("Value: " + x))
    .onFailure(e -> System.out.println("Error: " + e.message()));
```

### Context

```java
import noie.linmimeng.noiedigitalsystem.api.context.NdsContext;

var ctx = NdsContext.create();
System.out.println("TraceId: " + ctx.traceId());

var enriched = ctx.withMeta("user", "player123");
```

### Policy + Rationale

```java
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import noie.linmimeng.noiedigitalsystem.api.audit.NdsRationale;
import noie.linmimeng.noiedigitalsystem.api.policy.NdsPolicy;

var policy = NdsPolicy.of(
    "shop-default-policy-v1",
    "quota",
    Map.of(),
    new byte[0],
    Map.of()
);

var rationale = NdsRationale.of(
    "human_admin",
    BigDecimal.ONE,
    List.of("manual_review", "approved"),
    List.of(),
    List.of(),
    BigDecimal.ZERO,
    Map.of()
);
```

## Packages

| Artifact | Description |
|---------|-------------|
| `io.github.misty4119:noiedigitalsystem-api` | Java protocol types + adapters |

## Compatibility

- Java 21

## Notes (generated code)

- Protobuf DTOs are generated at build time from `../spec/proto/**`.
- Local generated sources are included via Gradle sourceSets:
  - `java/build/generated/source/proto/main/java` (not committed)

## Related Projects

- [nds-api](https://github.com/Misty4119/nds-api) - Protocol + SDKs monorepo

## License

Apache License 2.0

