# NDS-API General Java Guide

> **Scope**: This document targets non-Minecraft Java applications that want to integrate with NDS-API.
>
> **Language policy**: English-only (see `CONTRIBUTING.md`).  
> **Project inception**: 2025-12-22  
> **Last updated**: 2026-01-27

---

## Purpose

NDS-API is a **protocol layer** (types + contracts). This repository does **not** ship a runtime implementation.  
To execute queries or publish events, you must provide a runtime implementation (e.g., via `nds-core`).

This guide focuses on **mechanical integration**:

- Dependency coordinates
- Runtime acquisition (`NdsProvider` → `NdsRuntime`)
- Identity / Asset / Transaction primitives
- Async result handling (`CompletableFuture<NdsResult<T>>`)

---

## Installation (Maven / Gradle)

### Gradle (Kotlin)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.misty4119:noiedigitalsystem-api:2.0.0")
}
```

### Maven

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version>2.0.0</version>
</dependency>
```

---

## Runtime access

NDS-API uses `NdsProvider` as the entry point.

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

if (!NdsProvider.isInitialized()) {
    throw new IllegalStateException("NDS runtime is not initialized");
}

NdsRuntime runtime = NdsProvider.get();
```

---

## Core primitives (Identity / Asset / Transaction)

### Identity

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

NdsIdentity user = NdsIdentity.of("550e8400-e29b-41d4-a716-446655440000", IdentityType.PLAYER);
```

### AssetId

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

### Query balance (async, non-blocking)

```java
import java.math.BigDecimal;

runtime.query().queryBalance(coins, user)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // handle balance
        } else {
            // handle business failure
            System.err.println("Query failed: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        // handle system failure
        ex.printStackTrace();
        return null;
    });
```

### Publish transaction (async, non-blocking)

```java
import java.math.BigDecimal;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

NdsTransaction tx = NdsTransactionBuilder.create()
    .actor(user)
    .asset(coins)
    .delta(BigDecimal.valueOf(10))
    .consistency(ConsistencyMode.STRONG)
    .reason("deposit")
    .build();

runtime.eventBus().publish(tx)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            // persisted
        } else {
            System.err.println("Publish failed: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        ex.printStackTrace();
        return null;
    });
```

---

## Non-negotiable rules (mechanical)

- Use `BigDecimal` for economic values.
- Do not block on `CompletableFuture` (no `.get()`).
- Always check `NdsResult.isSuccess()` before accessing `.data()`.

---

## References

- Protocol: [`spec/docs/PROTOCOL.md`](spec/docs/PROTOCOL.md)
- Repo docs index: [`docs/README.md`](docs/README.md)
- TW guide: [`DEVELOPER_GUIDE_TW.md`](DEVELOPER_GUIDE_TW.md)

