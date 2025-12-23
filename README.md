# NDS Developer Guide - Next-Generation Economy Protocol

> **NDS – Next-Generation Economy Protocol (NGEP)**
> *The successor of Vault, not a replacement — a protocol.*

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

### Maintainer Statement

This developer guide is maintained by the **NDS Core Team** and is the official specification document for the NDS protocol.

### Final Interpretation Authority

Whether a plugin meets the "NDS-native Plugin" standard, **the final interpretation authority belongs to the NDS protocol owner**.

This protocol requires all plugin developers using NDS to follow the specifications in this guide. Plugins that violate this guide will be considered non-compliant with the NDS-native plugin standard.

### Compliance Determination

The determination criteria for NDS-native plugins are based on the "Must Do" and "Absolutely Forbidden" clauses explicitly listed in this guide. Any violation of these clauses will result in the plugin not meeting the NDS-native standard.

### Actual Impact Scope of Protocol Compliance

To avoid misunderstandings, we clearly distinguish the boundaries between "Guideline", "Certification", and "Enforcement":

1. **Technical Enforcement (Enforced)**: NDS core **will NOT** actively prevent non-compliant plugins from running. As long as API calls are legal, NDS will execute them.
2. **Official Certification (Certified)**: "NDS-native" is an official certification standard. Only plugins that meet the standard can receive official recommendations and the "NDS-native" mark.
3. **Guarantee Scope (Guaranteed)**: Non-compliant plugins can still use NDS, but do not enjoy future version compatibility, state consistency commitments, and performance optimization guarantees.

---

## 🎯 Design Principles

### 1. Single Source of Truth

All economic values, cross-plugin states, and transaction results **must exist only in NDS**.

👉 **Plugins always "request state" rather than "own state"**

### 2. Async-first by Design

- All APIs return `CompletableFuture`
- **Blocking is prohibited** (do not use `.get()` on the main thread)
- Built for high concurrency, Folia, and cross-server environments

### 3. Protocol First, Features Optional

NDS doesn't care:
- How shops sell
- How quests give rewards
- How auctions bid

NDS only cares:
> **Whether state is legal, consistent, and secure**

### 4. Safety & Consistency

- **BigDecimal precision**: Avoids double precision issues
- **Transaction atomicity**: Operations automatically roll back on failure
- **Cross-server synchronization**: Redis ensures multi-server consistency

### 5. Why No Synchronous Events? (Architectural Decision)

NDS **intentionally does not provide** synchronous event hooks (such as `PlayerPreWithdrawEvent`) for the following reasons:

1. **Prevent Race Conditions**: Synchronous events allow plugins to intercept and modify results, which is a major source of data inconsistency in async/cross-server environments.
2. **Force Result-driven Design**: Developers should focus on "operation results (Future)" rather than "operation process (Event)".

This is an architectural choice by NDS, not a missing feature.

---

## 🔒 Non-Negotiable Principles

The following principles are **non-negotiable and unchangeable** in all versions of NDS. These principles are the core foundation of the NDS protocol, and any changes that violate these principles will break protocol consistency.

### Principle 1: NDS is Always the Single Source of Truth

**Immutability**: This principle will never change in any version.

- NDS is the single source of truth for economic state
- Plugins **must not** manage any economic/state data themselves
- All state queries and modifications **must** go through NDS API
- Violating this principle will cause state inconsistency, and NDS does not guarantee correct behavior

### Principle 2: API is Always Async (Async-first)

**Immutability**: NDS will never provide synchronous APIs.

- All API methods **must** return `CompletableFuture`
- NDS **will not** provide any synchronous APIs (such as convenience methods for `.get()`)
- Plugins **must** use async callbacks to handle results
- Blocking Futures on the main thread is a **forbidden** design error

### Principle 3: Core Values Always Use BigDecimal

**Immutability**: NDS will never switch to double or other numeric types.

- All core numeric operations **must** use `BigDecimal`
- Precision guarantee is a core feature of the NDS protocol
- API methods **will not** accept `double` as primary parameters (only provide backward-compatible default methods)
- Using `double` for economic calculations is **forbidden**

### Principle 4: Vault is Always Just a Compatibility Layer

**Immutability**: Vault Bridge will never become a core feature of NDS.

- Vault Bridge is only for **legacy plugin compatibility**
- Newly developed plugins **must** use NDS API and must not use Vault API
- Plugins using Vault API will be considered Legacy Plugins
- NDS core evolution will revolve around NDS API, not Vault API

### Version Compatibility Guarantee

These principles will remain unchanged in all future versions of NDS. Any changes that violate these principles will be considered breaking changes and will be clearly marked as incompatible versions.

---

## 🔥 What is an NDS-native Plugin?

### NDS-native Plugin Definition

**NDS-native Plugin = Must simultaneously satisfy all of the following conditions:**

✅ **Must Do:**
- ✅ All state comes from NDS (do not store any economic/state data)
- ✅ All behavior is driven by "result callbacks" (async-first)
- ✅ Use NDS API directly (do not use Vault API)
- ✅ Properly handle exceptions and failure cases

❌ **Absolutely Forbidden:**
- ❌ Do not store any economic/state data locally
- ❌ Do not cache Digital values (state is managed by NDS)
- ❌ Do not assume state always exists (always check results)
- ❌ Do not use Vault API (new plugins must use NDS API directly)
- ❌ Do not block Futures on the main thread (using `.get()`)

### Why Become an NDS-native Plugin?

1. **State Consistency Guarantee**: All state is managed uniformly by NDS, preventing desynchronization
2. **Cross-server Compatibility**: Automatically supports multi-server environments
3. **Future Extensibility**: Can seamlessly use new NDS features
4. **Performance Optimization**: NDS internal optimizations automatically apply to your plugin

---

## ⚠️ Enforcement & Consequences

This section clearly states the consequences of violating NDS protocol specifications. These consequences are not threats, but necessary measures for protocol self-protection.

### Consequences of Not Meeting NDS-native Standards

If a plugin does not meet the "NDS-native Plugin" standard (violating "Must Do" or "Absolutely Forbidden" clauses), it will result in:

1. **Not Listed in Official Recommendations**
   - Will not appear in NDS official recommended plugin list
   - Will not receive the "NDS-native Plugin" certification mark

2. **No Guarantee of Compatibility with Future NDS Versions**
   - Future NDS evolution may cause these plugins to malfunction
   - Need to bear upgrade and maintenance costs themselves

3. **Do Not Enjoy NDS-native Plugin Advantages**
   - Cannot use new NDS features
   - Cannot receive NDS-native plugin performance optimizations
   - May experience state inconsistency issues in cross-server environments

### Consequences of Violating Core Principles

Violating "Non-Negotiable Principles" will result in:

1. **No Guarantee of Correct Behavior**
   - NDS cannot guarantee plugin state consistency
   - May experience serious issues like data loss, duplicate deductions, etc.

2. **May Cause State Inconsistency**
   - Cross-plugin states may become out of sync
   - State conflicts may occur in cross-server environments

3. **May Experience Issues in Cross-server Environments**
   - Redis synchronization may fail
   - State may become inconsistent in multi-server environments

### Legacy Plugin Positioning

New plugins using Vault API will be considered **Legacy Plugins**:

- **Only Considered as Legacy Plugin**: Will not be considered NDS-native plugins
- **Do Not Enjoy NDS-native Plugin Advantages**: Cannot use new NDS features and optimizations
- **Technical Debt**: Need to migrate to NDS API in the future
- **Maintenance Costs**: Need to handle Vault API limitations (precision, synchronization, etc.) themselves

### Official Recommendation Policy

NDS officially only recommends plugins that meet the following conditions:

- ✅ Fully comply with "NDS-native Plugin" standard
- ✅ Follow all "Non-Negotiable Principles"
- ✅ Pass NDS-native plugin checklist
- ✅ Use NDS API instead of Vault API

Plugins that do not meet the above conditions will not receive official recommendations but can still use NDS normally (through Vault Bridge or partial use of NDS API).

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
public void checkBalance(UUID uuid) {
    api.getPlayerDigitalAmount(uuid, "coins")
        .thenAccept(balance -> {
            // Use latest balance
        });
}
```

### ❌ Anti-Pattern 2: Assuming take Always Succeeds

```java
// ❌ Wrong: No result check
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100));
player.getInventory().addItem(new ItemStack(Material.DIAMOND)); // May give item for free
```

**Problem**: If balance is insufficient, the item has been given but money wasn't deducted.

**✅ Correct Approach**: Check result before executing

```java
// ✅ Correct: Check result
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            // Deduction successful, then give item
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        } else {
            player.sendMessage("Insufficient balance!");
        }
    });
```

### ❌ Anti-Pattern 3: Blocking Main Thread

```java
// ❌ Wrong: Blocking main thread
public void onPlayerCommand(Player player) {
    BigDecimal balance = api.getPlayerDigitalAmount(player.getUniqueId(), "coins").get(); // Blocking!
    player.sendMessage("Balance: " + balance);
}
```

**Problem**: Will cause server lag, violating async design principles.

**✅ Correct Approach**: Use callbacks

```java
// ✅ Correct: Async callback
public void onPlayerCommand(Player player) {
    api.getPlayerDigitalAmount(player.getUniqueId(), "coins")
        .thenAccept(balance -> {
            player.sendMessage("Balance: " + balance);
        })
        .exceptionally(ex -> {
            player.sendMessage("Query failed: " + ex.getMessage());
            return null;
        });
}
```

### ❌ Anti-Pattern 4: Check Then Deduct (Race Condition)

```java
// ❌ Wrong: Balance may be modified between check and deduction
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        if (balance.compareTo(BigDecimal.valueOf(100)) >= 0) {
            api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100)); // May fail
            giveItem();
        }
    });
```

**Problem**: Between check and deduction, balance may be modified by other operations.

**✅ Correct Approach**: Deduct directly, let NDS handle atomicity

```java
// ✅ Correct: Deduct directly, NDS guarantees atomicity
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            giveItem(); // Only give item if deduction succeeds
        } else {
            player.sendMessage("Insufficient balance!");
        }
    });
```

### ❌ Anti-Pattern 5: Not Handling Exceptions

```java
// ❌ Wrong: No exception handling
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // If error occurs, this won't execute, but also no error message
    });
```

**Problem**: Database errors, network issues, and other exceptions will be silently ignored.

**✅ Correct Approach**: Always handle exceptions

```java
// ✅ Correct: Handle exceptions
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // Success handling
    })
    .exceptionally(ex -> {
        getLogger().severe("Failed to query balance: " + ex.getMessage());
        player.sendMessage("Query failed, please try again later");
        return null;
    });
```

### ❌ Anti-Pattern 6: Using Vault API (New Plugins)

```java
// ❌ Wrong: New plugins should not use Vault API
Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
economy.withdrawPlayer(player, 100);
```

**Problem**: Vault API is synchronous, uses double precision, and doesn't support cross-server.

**✅ Correct Approach**: Use NDS API directly

```java
// ✅ Correct: Use NDS API directly
api.takePlayerDigital(player.getUniqueId(), "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        // Handle result
    });
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
Request NDS deduction (atomic operation)
↓
Success → Give reward
Failure → Provide feedback
```

**Advantages**:
- Fewer steps, clear logic
- NDS guarantees atomicity
- No Race Condition
- Automatically handles concurrency

### Practical Example

```java
// ✅ NDS-native design: Result-driven
public void onPlayerPurchase(Player player, ItemStack item, BigDecimal price) {
    UUID uuid = player.getUniqueId();
    
    // Deduct directly, let NDS handle all checks
    api.takePlayerDigital(uuid, "coins", price)
        .thenAccept(success -> {
            if (success) {
                // Deduction successful, give item
                player.getInventory().addItem(item);
                player.sendMessage("Purchase successful!");
            } else {
                // Deduction failed, inform reason
                api.getPlayerDigitalAmount(uuid, "coins")
                    .thenAccept(balance -> {
                        player.sendMessage("Insufficient balance! Current balance: " + balance);
                    });
            }
        })
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
| **Business Logic** | ❌ Doesn't care about business logic | ✅ Implement shop, quest, etc. logic |
| **UI / Feedback** | ❌ Doesn't provide UI | ✅ Provide player interface and messages |
| **Data Validation** | ✅ Validate value legality | ✅ Validate business rules (e.g., price) |

### Example: Shop Plugin

```java
// ✅ Correct responsibility division
public class ShopPlugin {
    
    // Plugin responsibility: Business logic and UI
    public void onPlayerClickShopItem(Player player, ShopItem item) {
        UUID uuid = player.getUniqueId();
        BigDecimal price = item.getPrice();
        
        // NDS responsibility: State management and atomic operations
        api.takePlayerDigital(uuid, "coins", price)
            .thenAccept(success -> {
                if (success) {
                    // Plugin responsibility: Give item and feedback
                    player.getInventory().addItem(item.getItemStack());
                    player.sendMessage("Purchase successful!");
                } else {
                    // Plugin responsibility: Inform failure reason
                    player.sendMessage("Insufficient balance!");
                }
            })
            .exceptionally(ex -> {
                // Plugin responsibility: Error handling and feedback
                getLogger().severe("Purchase failed: " + ex.getMessage());
                player.sendMessage("Purchase failed, please try again later");
                return null;
            });
    }
}
```

---

## 📦 Dependency Setup

Before you start using NDS API, you need to configure dependencies in your project.

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

### Getting API Instance

```java
import noie.linmimeng.noiedigitalsystem.NoieDigitalSystem;
import noie.linmimeng.noiedigitalsystem.api.NoieDigitalSystemAPI;

Plugin ndsPlugin = getServer().getPluginManager().getPlugin("NoieDigitalSystem");
if (ndsPlugin == null || !(ndsPlugin instanceof NoieDigitalSystem)) {
    getLogger().severe("NoieDigitalSystem not found!");
    return;
}

NoieDigitalSystem nds = (NoieDigitalSystem) ndsPlugin;
NoieDigitalSystemAPI api = nds.getAPI();
```

### Basic Operation Examples

#### Get Balance

```java
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        getLogger().info("Player balance: " + balance);
    })
    .exceptionally(ex -> {
        getLogger().severe("Failed to get balance: " + ex.getMessage());
        return null;
    });
```

#### Deduct (One-time Atomic Operation)

```java
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            // Deduction successful, give item
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        } else {
            // Insufficient balance
            player.sendMessage("Insufficient balance!");
        }
    })
    .exceptionally(ex -> {
        getLogger().severe("Failed to deduct: " + ex.getMessage());
        return false;
    });
```

#### Add Balance

```java
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(50))
    .thenRun(() -> {
        player.sendMessage("Received 50 coins!");
    })
    .exceptionally(ex -> {
        getLogger().severe("Failed to add balance: " + ex.getMessage());
        return null;
    });
```

---

## 📚 API Method Overview

### API Contract Definitions

When using methods like `takePlayerDigital` that return `CompletableFuture<Boolean>`, strictly follow these semantics:

* **`Boolean = false`**: **Business failure** (e.g., insufficient balance, deduction conditions not met). This is an expected business logic result.
* **`Exception`**: **System error** (e.g., database disconnection, Redis sync failure, concurrent write conflict). This is an unexpected system failure.

> **Important**: Plugins **must not** rely on Exception to judge business results (e.g., should not treat "database timeout" as "insufficient balance").

### Player Digital Operations (Most Common)

| Method | Description | Return Value |
|--------|-------------|--------------|
| `getPlayerDigitalAmount(uuid, name)` | Get balance | `CompletableFuture<BigDecimal>` |
| `givePlayerDigital(uuid, name, amount)` | Add balance | `CompletableFuture<Void>` |
| `takePlayerDigital(uuid, name, amount)` | Deduct (atomic operation) | `CompletableFuture<Boolean>` |
| `setPlayerDigitalAmount(uuid, name, amount)` | Set balance | `CompletableFuture<Void>` |
| `getAllPlayerDigitals(uuid)` | Get all Digitals | `CompletableFuture<Map<String, PlayerDigital>>` |

### Server Digital Operations (Server-wide Variables)

| Method | Description | Return Value |
|--------|-------------|--------------|
| `getServerDigitalAmount(name)` | Get server variable | `CompletableFuture<BigDecimal>` |
| `giveServerDigital(name, amount)` | Add server variable | `CompletableFuture<Void>` |
| `takeServerDigital(name, amount)` | Subtract server variable | `CompletableFuture<Boolean>` |
| `setServerDigitalAmount(name, amount)` | Set server variable | `CompletableFuture<Void>` |

### Global Player Digital (System Setup)

| Method | Description | Return Value |
|--------|-------------|--------------|
| `createGlobalPlayerDigital(name, initial, limit)` | Create global Digital | `CompletableFuture<Void>` |
| `isGlobalPlayerDigital(name)` | Check if global Digital | `CompletableFuture<Boolean>` |
| `getAllGlobalPlayerDigitals()` | Get all global Digitals | `CompletableFuture<Map<String, Digital>>` |

---

## ⚠️ Important Notes

### 1. Prohibit Blocking Main Thread

```java
// ❌ Wrong: Blocking main thread
BigDecimal balance = api.getPlayerDigitalAmount(uuid, "coins").get();

// ✅ Correct: Use CompletableFuture callback
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // Handle balance
    });
```

### 2. Must Handle Exceptions

**Requirement**: All API calls must handle exceptions. This is a mandatory requirement for NDS-native plugins.

```java
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // Success handling
    })
    .exceptionally(ex -> {
        // Must handle exceptions (mandatory requirement)
        getLogger().severe("Error: " + ex.getMessage());
        return null;
    });
```

**Consequence of Violation**: Plugins that don't handle exceptions do not meet NDS-native standards and may cause errors to be silently ignored.

### 3. Must Use BigDecimal, Prohibit Using double

**Requirement**: All economic numeric operations must use BigDecimal. This is one of the "Non-Negotiable Principles".

```java
// ❌ Wrong: Precision issues (violates Non-Negotiable Principle)
api.givePlayerDigital(uuid, "coins", 100.0);

// ✅ Correct: Must use BigDecimal
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(100));
```

**Consequence of Violation**: Using double for economic calculations violates Non-Negotiable Principles, and NDS does not guarantee correct behavior.

### 4. Digital Naming Convention

**Requirement**: Digital names must follow these conventions:

- **Must** use lowercase letters and underscores
- Examples: `coins`, `gold`, `stamina`, `world_boss_hp`
- **Forbidden**: `Coins`, `gold-coin`, `gold.coin`

**Consequence of Violation**: Digitals that don't follow naming conventions may not work properly.

---

## 🔌 Vault Compatibility (Legacy Support)

> **Important**: Vault Bridge is only for **legacy plugin compatibility**. Newly developed plugins **must** use NDS API directly and must not use Vault API.

NDS provides an **official Vault Bridge** for seamless integration of legacy plugins:

```
Old Plugin ── Vault ──┐
                      ▼
                 NDS Core
                      ▲
New Plugin ── NDS API ─┘
```

Legacy plugins "think they're using Vault", but state is managed by NDS.

### Configuring Vault Bridge

In `config.yml`:

```yaml
vault:
  enabled_digitals:
    - "coins"
    - "gems"
  default_currency: "coins"
```

---

## 📖 Phase-oriented Development Guide

### Phase 1: Legacy Plugin (Using Vault)

**Target Audience**: Existing Vault plugins that need seamless migration

- Continue using Vault API
- NDS provides state management transparently as backend
- No code modification needed

### Phase 2: NDS-native Plugin (Currently Required)

**Target Audience**: Newly developed plugins

**Requirements** (not suggestions):
- ✅ **Must** use NDS API directly
- ✅ **Must** follow all principles in this guide
- ✅ **Must** become NDS-native plugins

**Checklist** (all items must pass):
- [ ] **Must** not use Vault API
- [ ] **Must** not cache any state
- [ ] **Must** have all operations be async
- [ ] **Must** properly handle exceptions
- [ ] **Must** use result-driven design

**Note**: Plugins that don't meet this checklist will be considered non-compliant with NDS-native standards and will not enjoy official recommendations and future version compatibility guarantees.

### Phase 3: Advanced Features (Future)

**Target Audience**: Plugins requiring advanced features

- Multi-Digital collaborative operations
- Cross-system state management
- Custom Digital types

---

## 🏗️ Architecture Diagram

```
Plugins (Shop / Quest / RPG / Gacha)
        │
        ▼
┌──────────────────────┐
│   NDS API / Protocol │  ← Protocol Layer (You use this)
└──────────────────────┘
        │
 ┌──────┴────────┐
 │               │
 ▼               ▼
PostgreSQL      Redis
(State)         (Sync / Cache)
```

---

## 📖 More Resources

- **API Documentation**: See `NoieDigitalSystemAPI` interface JavaDoc
- **Example Plugins**: Refer to NDS official examples
- **Issue Reporting**: GitHub Issues

---

## 💡 Core Success Criteria

1. **After installing NDS, core economy works even without Web/Shop/Auction**
2. **Plugin authors can easily get started, no need to manage core state**
3. **Admins can freely choose to enable application layer features**
4. **Core API is stable and reliable under cross-server/high concurrency**
5. **Official Vault Bridge guarantees legacy plugin compatibility**

---

## 🎯 Summary: Becoming an NDS-native Plugin Author

When you follow this guide to develop plugins, you have become an **NDS-native Plugin Author**:

✅ **You no longer need to:**
- Manage economic state
- Handle precision issues
- Worry about cross-server synchronization
- Implement atomic transactions

✅ **You only need to:**
- Focus on business logic
- Use NDS API
- Handle result callbacks
- Provide user experience

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

