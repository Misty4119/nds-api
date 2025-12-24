# NDS API v2.0 Developer Guide - Next-Generation Economy Protocol

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *The successor of Vault, not a replacement — a protocol layer.*

---

## 📘 Core Positioning

**NDS is not another economy plugin, but rather the "Economy/State Protocol Layer" in the Minecraft ecosystem.**

Just like:
- **HTTP** for Web
- **JDBC** for databases
- **MCP** for AI tools

NDS provides a unified state management protocol, allowing plugin authors to focus on business logic without managing core economic state.

---

## ⚖️ Protocol Authority & Compliance

This guide is the official specification maintained by the **Noie Team**. The final interpretation authority belongs to the NDS protocol owner.

**Compliance Scope**:
- **Enforcement**: NDS will not prevent non-compliant plugins from running (legal API calls are executed)
- **Certification**: Only compliant plugins receive "NDS-native" mark and official recommendations
- **Guarantees**: Non-compliant plugins do not enjoy future compatibility, consistency, or performance guarantees

---

## 🎯 Design Principles (Bedrock Specification)

### 1. Protocol First

**API is a protocol, not a tool, not an implementation.**

- API layer has **zero dependencies** on Bukkit/Paper/Database/Network
- Only interfaces and contracts are defined
- Implementation is isolated in nds-core module

### 2. Event Is The Source Of Truth

**State can ONLY be obtained through event projection. Direct state modification is forbidden.**

- All state changes must go through events
- State is computed from event history (Event Sourcing)
- Any point in time can be reconstructed from historical events

### 3. Replayable By Design

**Any point in time can be reconstructed from historical events.**

- All events must be serializable
- Events are immutable historical records
- Projections are pure functions (no side effects)

### 4. AI-Ready Default

**All data structures must be semanticizable, vectorizable, and analyzable.**

- Support tags and metadata
- All assets and events can be semanticized
- Built for future AI analysis

### 5. Implementation Isolation

**API layer must not depend on concrete implementations.**

- Only interfaces and contracts are defined
- No Bukkit/Paper/Database/Network dependencies
- Protocol layer is completely isolated

---

## 🔒 Non-Negotiable Principles

The following principles are **non-negotiable and unchangeable** in all versions of NDS. These principles are the core foundation of the NDS protocol, and any changes that violate these principles will break protocol consistency.

### Principle 1: NDS is Always the Single Source of Truth

**Immutability**: This principle will never change in any version.

- NDS is the single source of truth for economic state
- Plugins **must not** manage any economic/state data themselves
- All state queries and modifications **must** go through NDS API
- State can only be obtained through event projection
- Violating this principle will cause state inconsistency, and NDS does not guarantee correct behavior

### Principle 2: API is Always Async (Async-first)

**Immutability**: NDS will never provide synchronous APIs.

- All API methods **must** return `CompletableFuture<NdsResult<T>>`
- NDS **will not** provide any synchronous APIs
- Plugins **must** use async callbacks to handle results
- Blocking Futures on the main thread is a **forbidden** design error
- Use `runtime.mainThreadExecutor()` when calling Bukkit API in callbacks

### Principle 3: Core Values Always Use BigDecimal

**Immutability**: NDS will never switch to double or other numeric types.

- All core numeric operations **must** use `BigDecimal`
- Precision guarantee is a core feature of the NDS protocol
- API methods **will not** accept `double` as primary parameters
- Using `double` for economic calculations is **forbidden**

### Principle 4: Event-Driven Architecture

**Immutability**: State changes must always go through events.

- All state changes **must** be expressed as events
- Events are immutable and serializable
- State is computed from event history
- Direct state modification is **forbidden**

### Principle 5: Result-Driven Error Handling

**Immutability**: Errors are expressed as `NdsResult`, not exceptions.

- Business failures are expressed as `NdsResult.isSuccess() == false`
- System errors are expressed as exceptions in `.exceptionally()`
- **Must** check `NdsResult.isSuccess()` before accessing `.data()`
- Do not use exceptions to determine business results

---

## 🔥 What is an NDS-native Plugin?

### NDS-native Plugin Definition

**NDS-native Plugin = Must simultaneously satisfy all of the following conditions:**

✅ **Must Do:**
- ✅ Use `NdsProvider.get()` to get `NdsRuntime` (the only entry point)
- ✅ All state comes from NDS (do not store any economic/state data)
- ✅ All behavior is driven by "result callbacks" (async-first)
- ✅ Use `NdsResult` for error handling (check `isSuccess()` before accessing `.data()`)
- ✅ Use `NdsTransactionBuilder` to create transactions
- ✅ Use `runtime.mainThreadExecutor()` when calling Bukkit API in callbacks
- ✅ Properly handle `NdsResult` failures with `.onFailure()` or `.exceptionally()`

❌ **Absolutely Forbidden:**
- ❌ Do not use `.get()` on `CompletableFuture` (blocks main thread)
- ❌ Do not use `double`, `float`, or `int` for economic values
- ❌ Do not store any economic/state data locally
- ❌ Do not cache balance or asset values
- ❌ Do not put Bukkit/JVM objects in `NdsPayload`
- ❌ Do not use deprecated `NoieDigitalSystemAPI` in new plugins
- ❌ Do not call Bukkit API directly in async callbacks
- ❌ Do not access `.data()` on failed `NdsResult` (check `isSuccess()` first)
- ❌ Do not modify state directly (only through events)


---

## ⚠️ Consequences of Violation

**Non-compliant plugins** (violating "Must Do" or "Absolutely Forbidden"):
- Not listed in official recommendations
- No future version compatibility guarantee
- Cannot use new NDS features or optimizations

**Violating core principles**:
- No guarantee of correct behavior (may cause data loss, inconsistency)
- State may desynchronize in cross-server environments

---

## 📦 Dependency Setup

Before you start using NDS API v2.0, you need to configure dependencies in your project.

### 1. Gradle (Kotlin DSL) - Recommended

```kotlin
repositories {
    // NDS Protocol Layer Repository
    maven("https://repo.repsy.io/mvn/linmimeng/releases")
}

dependencies {
    // Use compileOnly because NDS will provide this API at runtime
    compileOnly("noie.linmimeng:noiedigitalsystem-api:2.0.0")
}
```

### 2. Gradle (Groovy DSL)

```groovy
repositories {
    maven { url 'https://repo.repsy.io/mvn/linmimeng/releases' }
}

dependencies {
    compileOnly 'noie.linmimeng:noiedigitalsystem-api:2.0.0'
}
```

### 3. Maven

```xml
<repositories>
    <repository>
        <id>nds-repo</id>
        <url>https://repo.repsy.io/mvn/linmimeng/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>noie.linmimeng</groupId>
        <artifactId>noiedigitalsystem-api</artifactId>
        <version>2.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 🚀 Quick Start

### Getting Runtime Instance

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

// Check if NDS is initialized
if (!NdsProvider.isInitialized()) {
    getLogger().severe("NDS not initialized!");
    return;
}

NdsRuntime runtime = NdsProvider.get();
```

### Creating Identity

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

// Lightweight creation (no async query needed)
NdsIdentity player = NdsIdentity.fromString("550e8400-e29b-41d4-a716-446655440000");
// or
NdsIdentity player = NdsIdentity.of("550e8400-e29b-41d4-a716-446655440000", IdentityType.PLAYER);
```

### Creating Asset ID

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
// or
AssetId coins = AssetId.fromString("player:coins");
```

### Querying Balance

```java
import java.math.BigDecimal;

runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            player.sendMessage("Balance: " + balance);
        } else {
            player.sendMessage("Failed to query balance: " + result.error().message());
        }
    }, runtime.mainThreadExecutor())
    .exceptionally(ex -> {
        getLogger().severe("Error: " + ex.getMessage());
        return null;
    });
```

### Creating and Publishing Transaction

```java
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

// Create transaction
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))  // Positive = add, negative = subtract
    .consistency(ConsistencyMode.STRONG)
    .source(sourceIdentity)  // Optional, for transfers
    .target(targetIdentity)   // Optional, for transfers
    .reason("purchase")       // Optional
    .build();

// Publish transaction (Future completes when persisted)
runtime.eventBus().publish(transaction)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            // Transaction persisted successfully
            player.sendMessage("Transaction completed!");
        } else {
            player.sendMessage("Transaction failed: " + result.error().message());
        }
    }, runtime.mainThreadExecutor())
    .exceptionally(ex -> {
        getLogger().severe("Error: " + ex.getMessage());
        return null;
    });
```

---

## 📚 API Overview

**Error Handling**: `NdsResult.isSuccess() == false` = business failure (expected), `Exception` = system error (unexpected). Do not use exceptions to judge business results.

**Core Services**:
- `runtime.query()` - Query state through projections
- `runtime.eventBus()` - Publish events (Future completes when persisted)
- `runtime.identity()` - Identity management

**Key Methods**:
- `queryBalance(assetId, identity)` → `CompletableFuture<NdsResult<BigDecimal>>`
- `publish(event)` → `CompletableFuture<NdsResult<Void>>`
- `NdsTransactionBuilder.create().actor().asset().delta().consistency().build()`

---

## ❌ Common Anti-Patterns

### ❌ Anti-Pattern 1: Caching Balance in Plugin

```java
// ❌ Wrong: Caching causes state desynchronization
private final Map<UUID, BigDecimal> balanceCache = new HashMap<>();

public void checkBalance(UUID uuid) {
    if (balanceCache.containsKey(uuid)) {
        return balanceCache.get(uuid); // Wrong: may be outdated
    }
    // ...
}
```

**Problem**: Other plugins or servers may have modified the balance, and caching will cause state inconsistency.

**✅ Correct Approach**: Always query from NDS

```java
// ✅ Correct: Always query latest state
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // Use latest balance
        }
    });
```

### ❌ Anti-Pattern 2: Not Checking NdsResult Success

```java
// ❌ Wrong: No result check
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        BigDecimal balance = result.data(); // Throws if failed!
    });
```

**Problem**: If query fails, accessing `.data()` will throw `IllegalStateException`.

**✅ Correct Approach**: Check result before accessing data

```java
// ✅ Correct: Check result
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // Use balance
        } else {
            // Handle failure
            result.onFailure(error -> {
                getLogger().severe("Failed: " + error.message());
            });
        }
    });
```

### ❌ Anti-Pattern 3: Blocking Main Thread

```java
// ❌ Wrong: Blocking main thread
public void onPlayerCommand(Player player) {
    NdsResult<BigDecimal> result = runtime.query().queryBalance(assetId, identity).get(); // Blocking!
    BigDecimal balance = result.data();
    player.sendMessage("Balance: " + balance);
}
```

**Problem**: Will cause server lag, violating async design principles.

**✅ Correct Approach**: Use callbacks

```java
// ✅ Correct: Async callback
public void onPlayerCommand(Player player) {
    runtime.query().queryBalance(assetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                player.sendMessage("Balance: " + result.data());
            }
        }, runtime.mainThreadExecutor())
        .exceptionally(ex -> {
            player.sendMessage("Query failed: " + ex.getMessage());
            return null;
        });
}
```

### ❌ Anti-Pattern 4: Calling Bukkit API in Async Thread

```java
// ❌ Wrong: Bukkit API in async thread
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        player.sendMessage("Balance: " + result.data()); // May throw!
    });
```

**Problem**: Bukkit API is not thread-safe. All Bukkit operations must run on the main thread.

**✅ Correct Approach**: Use main thread executor

```java
// ✅ Correct: Use main thread executor
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        player.sendMessage("Balance: " + result.data());
    }, runtime.mainThreadExecutor());
```

### ❌ Anti-Pattern 5: Using Primitive Types Instead of BigDecimal

```java
// ❌ Wrong: Precision issues
double price = 100.5;
int amount = 100;

// ✅ Correct: Must use BigDecimal
BigDecimal price = BigDecimal.valueOf(100.5);
BigDecimal amount = BigDecimal.valueOf(100);
// or
BigDecimal price = new BigDecimal("100.5");
```

### ❌ Anti-Pattern 6: Putting Bukkit Objects in Payload

```java
// ❌ Wrong: Bukkit objects in payload
NdsPayload payload = NdsPayload.builder()
    .put("item", itemStack)  // Illegal!
    .put("location", location)  // Illegal!
    .build();

// ✅ Correct: Only primitive types
NdsPayload payload = NdsPayload.builder()
    .put("itemId", "diamond")
    .put("world", "world")
    .put("x", location.getX())
    .put("y", location.getY())
    .put("z", location.getZ())
    .build();
```

### ❌ Anti-Pattern 7: Using EventBuilder for Transactions

```java
// ❌ Wrong: Cannot cast
NdsEvent event = NdsEventBuilder.create()
    .actor(identity)
    .type(EventType.TRANSACTION)
    .build();
NdsTransaction transaction = (NdsTransaction) event; // Compilation error!

// ✅ Correct: Use TransactionBuilder
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .build();
```

---

## 🎨 Design Pattern: Result-driven Design

### Traditional Design (Forbidden)

```text
Player clicks purchase
↓
Check balance first
↓
Then deduct
↓
Then give item
```

**Problem**: Many steps, error-prone, has Race Condition risk. This design violates NDS protocol principles.

### NDS-native Design (Required)

```text
Player clicks purchase
↓
Create and publish transaction (atomic operation)
↓
Success → Give reward
Failure → Provide feedback
```

**Advantages**:
- Fewer steps, clear logic
- NDS guarantees atomicity
- No Race Condition
- Automatically handles concurrency
- Full audit trail through events

### Practical Example

```java
// ✅ NDS-native design: Result-driven
public void onPlayerPurchase(Player player, AssetId itemAssetId, BigDecimal price) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // Create transaction (negative delta = deduction)
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(identity)
        .asset(coinsAssetId)
        .delta(price.negate())  // Negative = subtract
        .consistency(ConsistencyMode.STRONG)
        .reason("purchase:" + itemAssetId.name())
        .build();
    
    // Publish transaction
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // Transaction succeeded, give item
                player.getInventory().addItem(itemStack);
                player.sendMessage("Purchase successful!");
            } else {
                // Transaction failed (e.g., insufficient balance)
                player.sendMessage("Purchase failed: " + result.error().message());
            }
        }, runtime.mainThreadExecutor())
        .exceptionally(ex -> {
            // Handle exception
            getLogger().severe("Purchase failed: " + ex.getMessage());
            player.sendMessage("Purchase failed, please try again later");
            return null;
        });
}
```

---

## 📋 Responsibility Boundaries

Clearly understand "what is NDS's responsibility and what is the plugin's responsibility":

| Responsibility Item | NDS Responsible | Plugin Responsible |
|---------------------|----------------|-------------------|
| **State Consistency** | ✅ Guarantee all state consistent | ❌ Should not manage state themselves |
| **Precision Handling** | ✅ Use BigDecimal for precision | ❌ Should not use double |
| **Atomic Transactions** | ✅ Guarantee operation atomicity | ❌ Should not implement transaction logic themselves |
| **Cross-server Sync** | ✅ Automatically sync multiple servers | ❌ Should not handle synchronization themselves |
| **Event Sourcing** | ✅ Store and replay events | ❌ Should not manage event storage |
| **Business Logic** | ❌ Doesn't care about business logic | ✅ Implement shop, quest, etc. logic |
| **UI / Feedback** | ❌ Doesn't provide UI | ✅ Provide player interface and messages |
| **Data Validation** | ✅ Validate value legality | ✅ Validate business rules (e.g., price) |


---

## ⚠️ Critical Rules

1. **Never block main thread**: Use callbacks, never `.get()`
2. **Always check `NdsResult.isSuccess()`** before accessing `.data()`
3. **Always use `BigDecimal`** for economic values (never `double`)
4. **Asset names**: lowercase + underscores only (e.g., `coins`, `world_boss_hp`)
5. **Event publish**: Future completes when **persisted**, not just queued

---

## 🏗️ Architecture Diagram

```
Your Plugin
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← Protocol Layer (this module)
│   (Interfaces Only)  │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← Implementation (nds-core module)
│   (Event Store,     │
│    Projections)     │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(Event Store)  (Sync / Cache)
```

**Key Points**:
- NDS API v2.0 is a **protocol layer**, not an implementation
- Your plugin **requests state** from NDS, doesn't **own state**
- All state changes go through **events** (Event Sourcing)
- State is obtained through **projections** (not direct reads)
- NDS handles: precision, atomicity, cross-server sync, replayability

---

## 📖 Resources

- **JavaDoc**: See interface comments

---

## 🎯 Summary: Becoming an NDS-native Plugin Author

When you follow this guide to develop plugins, you have become an **NDS-native Plugin Author**:

✅ **You no longer need to:**
- Manage economic state
- Handle precision issues
- Worry about cross-server synchronization
- Implement atomic transactions
- Store event history

✅ **You only need to:**
- Focus on business logic
- Use NDS API v2.0 (`NdsProvider`, `NdsRuntime`, `NdsTransactionBuilder`)
- Handle `NdsResult` callbacks
- Provide user experience
- Create and publish events

---

## 📜 Protocol Statement

> **NDS unifies "correctness, state, cross-server consistency, and future expansion"**
> 
> **This guide is not "suggestions", but "protocol specifications".**
> 
> **Plugins that violate this guide will not meet NDS-native plugin standards,**
> **and will not enjoy official recommendations and future version compatibility guarantees.**
> 
> **When you start following "forbidden items" instead of "suggested items",**
> **you truly understand the meaning of NDS as a protocol layer.**
> 
> **When you start using this guide to "say no",**
> **you truly exercise the power of the protocol owner.**

---

## 🔄 Migration from v1.0

**Key Changes**:
- Entry: `NdsProvider.get()` → `NdsRuntime` (not `NoieDigitalSystem.getAPI()`)
- Error handling: `NdsResult<T>` (not `CompletableFuture<Boolean>`)
- Transactions: `NdsTransactionBuilder` (not direct API calls)
- Identity: `NdsIdentity` (not `UUID`)
- Assets: `AssetId` (not string names)

**Migration**: Old API deprecated but available via bridge. New plugins **must** use v2.0.

---

**Version**: 2.0.0  
**Last Updated**: 2025-12-24  
**Status**: ✅ Stable
