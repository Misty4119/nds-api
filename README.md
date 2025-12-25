# NDS-API: Lightweight Event Sourcing Financial Framework

> **NDS (Noie Digital System) - High-Performance, Embedded, Event-Sourced Asset Management Protocol Layer**

[![Maven Central](https://img.shields.io/maven-central/v/noie.linmimeng/noiedigitalsystem-api)](https://search.maven.org/artifact/noie.linmimeng/noiedigitalsystem-api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)

---

> **🎮 Looking for the Minecraft Plugin Documentation?**  
> [Click here → Minecraft Developer Guide](./DEVELOPER_GUIDE_TW.md)  
> This README is for general Java developers. Minecraft plugin developers should refer to the dedicated guide.

---

NDS-API is a pure Java event sourcing framework designed for high-concurrency, low-latency financial systems. It provides comprehensive asset management, transaction processing, and state querying capabilities without requiring large frameworks (such as Kafka or Axon).

Originally designed for the Minecraft game ecosystem, NDS-API has been validated through years of high-concurrency scenarios and has evolved into a general-purpose Java framework suitable for enterprise applications.

## ✨ Core Features

- **Event Sourcing**: All state changes are recorded through immutable events
- **ACID Guarantees**: Strong consistency transactions with support for rollback
- **High Concurrency**: Non-blocking asynchronous API supporting thousands of concurrent requests
- **Precise Calculations**: Uses BigDecimal with no floating-point errors
- **Audit Logging**: Complete operation history compliant with financial regulations
- **Lightweight**: No external message queue required; runs in a single Java process
- **Embedded**: Can be embedded as a library in any Java application

## 🎯 Use Cases

- **Microservice Internal State Management**: Asset/balance management within services
- **Financial Technology (FinTech)**: Wallet systems, payment processing, balance queries
- **E-Commerce Systems**: Points systems, coupons, member balances
- **Game Economics**: Virtual currency, item trading (Minecraft plugin ecosystem)
- **Internet of Things (IoT)**: Device state tracking, resource allocation

## 🚀 Quick Start

### Maven

```xml
<dependency>
    <groupId>noie.linmimeng</groupId>
    <artifactId>noiedigitalsystem-api</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```kotlin
dependencies {
    implementation("noie.linmimeng:noiedigitalsystem-api:2.0.0")
}
```

**Note**: Currently published to Repsy Maven repository. Maven Central publication is in progress.

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.repsy.io/mvn/linmimeng/releases")
}
```

### Basic Usage

```java
import noie.linmimeng.noiedigitalsystem.api.*;
import java.math.BigDecimal;

// Check if NDS is initialized
if (!NdsProvider.isInitialized()) {
    System.err.println("NDS not initialized!");
    return;
}

// Get runtime instance
NdsRuntime runtime = NdsProvider.get();

// Create identity (account)
NdsIdentity account = NdsIdentity.of("user-123", IdentityType.ACCOUNT);

// Create asset ID
AssetId wallet = AssetId.of(AssetScope.ACCOUNT, "wallet");

// Query balance (async, non-blocking)
runtime.query().queryBalance(wallet, account)
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

// Create transaction
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(account)
    .asset(wallet)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("deposit")
    .build();

// Publish transaction (Future completes when persisted)
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

## 🏗️ Architecture Design

NDS-API adopts a **protocol layer design** with complete separation from implementation:

```
Your Application
    │
    ▼
┌──────────────────────┐
│   NDS-API            │  ← Protocol Layer (Pure Interfaces, Zero Dependencies)
│   (Interfaces Only)  │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS-Core           │  ← Implementation Layer (Event Store, Projections)
│   (Implementation)   │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(Event Store)  (Cache)
```

**Key Design Principles**:
- **Protocol Layer**: API layer defines only interfaces and contracts with zero dependencies
- **Implementation Isolation**: Implementation layer (nds-core) is completely separated from protocol layer
- **Platform Abstraction**: Supports different runtime environments through `NdsPlatform` interface (Minecraft, Spring Boot, Standard Java)
- **Event Sourcing**: All state changes are recorded as immutable events
- **Async-First**: All API methods return `CompletableFuture<NdsResult<T>>` and never block
- **Precision Guaranteed**: All numeric operations use `BigDecimal` for financial-grade accuracy

## 📚 Core Concepts

### Event Sourcing

All state changes are recorded through **immutable events**. State is computed from event history through **projections**.

**Advantages**:
- Complete audit trail
- Replayable to any point in time
- No risk of state loss

### Async-First

All API methods return `CompletableFuture<NdsResult<T>>` and never block the calling thread.

**Advantages**:
- High concurrency performance
- Non-blocking I/O
- Suitable for microservice architecture

### Precise Calculations (BigDecimal)

All numeric operations use `BigDecimal` with no floating-point errors.

**Advantages**:
- Financial-grade precision
- No accumulated errors
- Compliance-ready

### Result-Driven Error Handling

Business failures are expressed through `NdsResult.isSuccess() == false`, while system errors are expressed through exceptions.

**Advantages**:
- Clear error semantics
- Type-safe error handling
- Aligns with functional programming paradigms

## 🎨 Why Choose NDS-API?

### vs Axon Framework

| Feature | NDS-API | Axon Framework |
|---------|---------|----------------|
| **Deployment Complexity** | Low (embedded) | High (requires message queue) |
| **Learning Curve** | Low | Medium to High |
| **Use Case** | Small to medium applications, microservice internal state | Large distributed systems |
| **Infrastructure** | None required | Message queue required |

**Choose NDS-API if**: You need a simple, embedded solution without complex event bus requirements.

### vs Kafka

| Feature | NDS-API | Kafka |
|---------|---------|-------|
| **Deployment Complexity** | Low (single machine) | High (requires cluster) |
| **Latency** | Low (direct database) | Medium (network overhead) |
| **Use Case** | Application internal state management | Cross-service message passing |
| **Infrastructure** | PostgreSQL + Redis | Kafka cluster |

**Choose NDS-API if**: You only need internal state management within your application, not cross-service messaging.

### vs Custom Solutions

| Feature | NDS-API | Custom Solution |
|---------|---------|-----------------|
| **Development Time** | Ready to use | Months of development |
| **Test Coverage** | Battle-tested | Requires own testing |
| **Maintenance Cost** | Community maintained | Self-maintained |
| **Feature Completeness** | Complete | Requires gradual implementation |

**Choose NDS-API if**: You want a proven, production-ready solution without reinventing the wheel.

## 📖 Complete Documentation

- **General Java Developer Guide**: [GENERAL_JAVA_GUIDE.md](./GENERAL_JAVA_GUIDE.md) - For general Java developers (Spring Boot, microservices, etc.)
- **Minecraft Plugin Developer Guide**: [DEVELOPER_GUIDE_TW.md](./DEVELOPER_GUIDE_TW.md) - For Minecraft plugin developers (繁體中文)
- **API Reference**: JavaDoc (In Progress)
- **Minecraft Migration Guide**: [nds_migration_next_gen_vault_replacement_plan_for_minecraft.md](./nds_migration_next_gen_vault_replacement_plan_for_minecraft.md) - Migration guide from v1.0 or other economy plugins

## 🔗 Related Resources

- **GitHub Repository**: [https://github.com/Misty4119/nds-api](https://github.com/Misty4119/nds-api)
- **Maven Repository**: [Repsy](https://repo.repsy.io/mvn/linmimeng/releases) (Current)
- **Maven Central**: [Coming Soon](https://search.maven.org/) (Publication in progress)

## 📄 License

Apache License 2.0

## 🙏 Acknowledgments

NDS-API was originally designed for the Minecraft game ecosystem. After years of validation in high-concurrency scenarios, it has evolved into a general-purpose Java framework.

---

## 📋 Terminology Mapping

| Minecraft Terminology | General Java Terminology | Description |
|----------------------|-------------------------|-------------|
| Player | Account / Entity | Account or entity |
| Economy Plugin | Distributed Ledger System | Distributed ledger system |
| Coins / Money | Assets / Currency | Assets or currency |
| Server Performance | High Concurrency & Low Latency | High concurrency and low latency |
| Plugin | Library / Framework / Module | Library, framework, or module |
| Server | Node / Microservice | Node or microservice |
| Command | API Endpoint / CLI | API endpoint or command-line interface |
| Config.yml | Properties / YAML Configuration | Properties or YAML configuration |
| Vault | Payment Gateway Interface | Payment gateway interface |
| Main Thread | Event Loop / Dispatcher Thread | Event loop or dispatcher thread |
| Scheduler | Executor / Task Scheduler | Executor or task scheduler |

---

**Version**: 2.0.0  
**Last Updated**: 2025-01-XX  
**Status**: ✅ Stable

---

## 📝 Additional Notes

### Maven Central Publication

- **Status**: Publication in progress
- **Current Repository**: [Repsy Maven Repository](https://repo.repsy.io/mvn/linmimeng/releases)
- **Future Group ID**: `io.github.misty4119` (GitHub namespace) or `com.linmimeng` (domain namespace)

### Platform Support

NDS-API supports multiple runtime environments through the `NdsPlatform` interface:

- **Minecraft** (Spigot/Paper/Folia): Implemented via `SpigotPlatform` in `nds-api-plugin`
- **Spring Boot**: `SpringNdsPlatform` (coming soon in `nds-spring-boot-starter`)
- **Standard Java**: Implemented via `StandardJavaPlatform` in `nds-core`

### API Evolution

- **v2.0.0**: Introduced `defaultExecutor()` and `platform()` methods
- **Backward Compatibility**: `mainThreadExecutor()` is deprecated but still available for compatibility
- **Migration**: Existing code using `mainThreadExecutor()` will continue to work, but new code should use `defaultExecutor()`
