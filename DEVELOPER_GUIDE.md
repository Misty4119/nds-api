# NDS API v2.0 Developer Guide - Next-Generation Economy Protocol

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *A protocol layer designed for cross-platform state management.*

---

## 📘 Core Positioning

**NDS is a cross-platform "Economy/State Protocol Layer".**

Similar to:
- **HTTP** for the Web
- **JDBC** for databases
- **MCP** for AI tools

NDS provides a unified state management protocol, allowing developers to focus on business logic without managing core economic state.

### Supported Platforms

| Platform | SDK | Status |
|----------|-----|--------|
| Java (General) | `noiedigitalsystem-api` | ✅ Stable |
| .NET / C# | `Noie.Nds.Api` | ✅ Stable |
| Minecraft | See [MINECRAFT_DEVELOPER_GUIDE.md](./MINECRAFT_DEVELOPER_GUIDE.md) | ✅ Stable |

---

## ⚖️ Protocol Adjudication and Compliance

This guide is the official specification maintained by the **Noie Team**. Final interpretation rights belong to the NDS protocol owners.

**Compliance Scope**:
- **Enforcement**: NDS will not prevent non-compliant applications from running (valid API calls will be executed)
- **Official Certification**: Only compliant applications can receive the "NDS-native" designation and official recommendation
- **Coverage**: Non-compliant applications do not enjoy future compatibility, consistency, or performance guarantees

---

## 🎯 Design Principles (Bedrock Specification)

### 1. Protocol First

**The API is a protocol, not a tool, not an implementation.**

- API layer has **zero dependencies** on platform-specific frameworks
- Only defines interfaces and contracts
- Implementation is isolated in core modules

### 2. Event Is The Source Of Truth

**State can only be obtained through event projection. Direct state modification is forbidden.**

- All state changes must go through events
- State is calculated from event history (Event Sourcing)
- Any point in time can be reconstructed from historical events

### 3. Replayable By Design

**Any point in time can be reconstructed from historical events.**

- All events must be serializable
- Events are immutable historical records
- Projections are pure functions (no side effects)

### 4. AI-Ready Default

**All data structures must be semanticizable, vectorizable, and analyzable.**

- Support for tags and metadata
- All assets and events are semanticizable
- Built for future AI analysis

### 5. Implementation Isolation

**The API layer must not depend on concrete implementations.**

- Only defines interfaces and contracts
- No platform-specific dependencies
- Complete protocol layer isolation

---

## 🔒 Inviolable Principles

The following principles are **non-negotiable and immutable** across all versions of NDS.

### Principle 1: NDS Is Always The Single Source of Truth

- NDS is the single source of truth for economic state
- Applications **must not** manage any economic/state data themselves
- All state queries and modifications **must** go through the NDS API
- State can only be obtained through event projection

### Principle 2: API Is Always Async-First

- **Java**: All API methods return `CompletableFuture<NdsResult<T>>`
- **C#**: All API methods return `Task<NdsResult<T>>`
- Blocking the calling thread is a **forbidden** design error

### Principle 3: Core Values Always Use Exact Types

- **Java**: Must use `BigDecimal`
- **C#**: Must use `decimal`
- Using `double` or `float` for economic calculations is forbidden

### Principle 4: Event-Driven Architecture

- All state changes **must** be expressed as events
- Events are immutable and serializable
- Direct state modification is **forbidden**

### Principle 5: Result-Oriented Error Handling

- Business failures are expressed as `NdsResult.IsSuccess == false`
- System errors are expressed as exceptions
- **Must** check result status before accessing data

---

## 📦 Dependency Setup

### Java SDK

#### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.misty4119:noiedigitalsystem-api:2.1.0")
}
```

#### Maven

```xml
<dependency>
    <groupId>io.github.misty4119</groupId>
    <artifactId>noiedigitalsystem-api</artifactId>
    <version>2.1.0</version>
</dependency>
```

### C# SDK

#### NuGet

```bash
dotnet add package Noie.Nds.Api --version 2.1.0
```

#### PackageReference

```xml
<PackageReference Include="Noie.Nds.Api" Version="2.1.0" />
```

---

## 🚀 Quick Start

### Java SDK

#### Get Runtime Instance

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

if (!NdsProvider.isInitialized()) {
    throw new IllegalStateException("NDS not initialized");
}

NdsRuntime runtime = NdsProvider.get();
```

#### Create Identity and Asset

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

// Create identity
NdsIdentity user = NdsIdentity.of("user-123", IdentityType.PLAYER);

// Create asset ID
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

#### Query Balance

```java
import java.math.BigDecimal;

runtime.query().queryBalance(coins, user)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            System.out.println("Balance: " + balance);
        } else {
            System.err.println("Query failed: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        System.err.println("System error: " + ex.getMessage());
        return null;
    });
```

#### Create and Publish Transaction

```java
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(user)
    .asset(coins)
    .delta(BigDecimal.valueOf(100))  // Positive = add, negative = subtract
    .consistency(ConsistencyMode.STRONG)
    .reason("deposit")
    .build();

runtime.eventBus().publish(transaction)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            System.out.println("Transaction completed");
        } else {
            System.err.println("Transaction failed: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        System.err.println("System error: " + ex.getMessage());
        return null;
    });
```

---

### C# SDK

#### Get Runtime Instance

```csharp
using Noie.Nds.Api;
using Noie.Nds.Api.Identity;
using Noie.Nds.Api.Asset;

// Get runtime from dependency injection
INdsRuntime runtime = serviceProvider.GetRequiredService<INdsRuntime>();
```

#### Create Identity and Asset

```csharp
// Create identity
INdsIdentity user = NdsIdentity.Of("user-123", IdentityType.Player);

// Create asset ID
IAssetId coins = AssetId.Of(AssetScope.Player, "coins");
```

#### Query Balance

```csharp
var result = await runtime.Query.QueryBalanceAsync(coins, user);

if (result.IsSuccess)
{
    decimal balance = result.Data;
    Console.WriteLine($"Balance: {balance}");
}
else
{
    Console.WriteLine($"Query failed: {result.Error.Message}");
}
```

#### Create and Publish Transaction

```csharp
using Noie.Nds.Api.Transaction;

var transaction = NdsTransactionBuilder.Create()
    .Actor(user)
    .Asset(coins)
    .Delta(100m)  // Positive = add, negative = subtract
    .Consistency(ConsistencyMode.Strong)
    .Reason("deposit")
    .Build();

var result = await runtime.EventBus.PublishAsync(transaction);

if (result.IsSuccess)
{
    Console.WriteLine("Transaction completed");
}
else
{
    Console.WriteLine($"Transaction failed: {result.Error.Message}");
}
```

---

## 📚 API Overview

### Core Services

| Service | Java | C# | Description |
|---------|------|-----|-------------|
| Query Service | `runtime.query()` | `runtime.Query` | Query state through projections |
| Event Bus | `runtime.eventBus()` | `runtime.EventBus` | Publish events |
| Identity Service | `runtime.identity()` | `runtime.Identity` | Identity management |

### Key Methods

#### Java

```java
// Query balance
CompletableFuture<NdsResult<BigDecimal>> queryBalance(AssetId asset, NdsIdentity identity);

// Publish event
CompletableFuture<NdsResult<Void>> publish(NdsEvent event);

// Build transaction
NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(amount)
    .consistency(mode)
    .build();
```

#### C#

```csharp
// Query balance
Task<NdsResult<decimal>> QueryBalanceAsync(IAssetId asset, INdsIdentity identity);

// Publish event
Task<NdsResult<Unit>> PublishAsync(INdsEvent @event);

// Build transaction
NdsTransactionBuilder.Create()
    .Actor(identity)
    .Asset(assetId)
    .Delta(amount)
    .Consistency(mode)
    .Build();
```

---

## ❌ Common Anti-Patterns

### Anti-Pattern 1: Caching Balances in Application

```java
// ❌ Wrong: Caching leads to state desynchronization
private final Map<String, BigDecimal> balanceCache = new HashMap<>();
```

**Problem**: Other services or nodes may have modified the balance. Caching leads to state inconsistency.

**✅ Correct approach**: Always query from NDS.

### Anti-Pattern 2: Not Checking Result Status

```java
// ❌ Wrong: Not checking result
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        BigDecimal balance = result.data(); // Throws exception on failure!
    });
```

**✅ Correct approach**: Check result before accessing data.

```java
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
        } else {
            // Handle failure
        }
    });
```

### Anti-Pattern 3: Blocking the Calling Thread

```java
// ❌ Wrong: Blocking thread
NdsResult<BigDecimal> result = runtime.query().queryBalance(assetId, identity).get();
```

```csharp
// ❌ Wrong: Blocking thread
var result = runtime.Query.QueryBalanceAsync(assetId, identity).Result;
```

**✅ Correct approach**: Use async callbacks or await.

### Anti-Pattern 4: Using Floating-Point Numbers

```java
// ❌ Wrong: Precision issues
double price = 100.5;
```

```csharp
// ❌ Wrong: Precision issues
double price = 100.5;
```

**✅ Correct approach**:

```java
// Java
BigDecimal price = new BigDecimal("100.5");
```

```csharp
// C#
decimal price = 100.5m;
```

---

## 🎨 Design Pattern: Result-Oriented Design

### Traditional Design (Forbidden)

```text
User requests operation
↓
Check balance first
↓
Then deduct
↓
Then execute operation
```

**Problems**: Many steps, error-prone, race condition risks.

### NDS-Native Design (Required)

```text
User requests operation
↓
Create and publish transaction (atomic operation)
↓
Success → Execute follow-up operations
Failure → Provide feedback
```

**Advantages**:
- Fewer steps, clear logic
- NDS guarantees atomicity
- No race conditions
- Automatic concurrency handling
- Complete audit trail through events

---

## 📋 Responsibility Boundaries

| Responsibility | NDS Responsible | Application Responsible |
|----------------|-----------------|-------------------------|
| **State Consistency** | ✅ Guarantees all state consistency | ❌ Should not manage state |
| **Precision Handling** | ✅ Uses exact types for precision | ❌ Should not use floating-point |
| **Atomic Transactions** | ✅ Guarantees operation atomicity | ❌ Should not implement transaction logic |
| **Cross-Node Sync** | ✅ Auto-sync across multiple nodes | ❌ Should not handle sync |
| **Event Sourcing** | ✅ Stores and replays events | ❌ Should not manage event storage |
| **Business Logic** | ❌ Not concerned with business logic | ✅ Implements application logic |
| **User Interface** | ❌ Does not provide UI | ✅ Provides user interface |

---

## 📋 Naming Rules

### Asset Names

- **Must** use lowercase letters and underscores only
- **Examples**: `coins`, `gold`, `stamina`, `world_boss_hp`
- **Forbidden**: `Coins`, `gold-coin`, `gold.coin`, `GOLD`

### Identity IDs

- **User**: UUID format (e.g., `550e8400-e29b-41d4-a716-446655440000`)
- **System**: `system:name` format (e.g., `system:admin`)
- **AI**: `ai:name` format (e.g., `ai:gpt-4`)
- **External**: `external:service:name` format (e.g., `external:payment:stripe`)

---

## 🏗️ Architecture Diagram

```
Your Application
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← Protocol Layer (this module)
│   (Interfaces Only)  │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← Implementation
│   (Event Store,     │
│    Projections)     │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(Event Store)  (Sync / Cache)
```

---

## 📖 Additional Resources

- **Minecraft Developer Guide**: [MINECRAFT_DEVELOPER_GUIDE.md](./MINECRAFT_DEVELOPER_GUIDE.md)
- **Traditional Chinese Guide**: [DEVELOPER_GUIDE_TW.md](./DEVELOPER_GUIDE_TW.md)

---

## 📜 Protocol Declaration

> **NDS unifies "correctness, state, cross-node consistency, and future extensibility"**
> 
> **This guide is not "advice"; it is "protocol specification".**
> 
> **Applications that violate this guide will not meet NDS-native standards,**
> **and will not receive official recommendations or future version compatibility guarantees.**

---

**Version**: 2.1.0  
**Project Inception**: 2025-12-22  
**Last Updated**: 2026-01-27  
**Status**: ✅ Stable
