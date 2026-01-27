# NDS Migration & Next-Generation Economy Protocol Plan

## 1. Purpose & Vision

### 1.1 Background

NoieDigitalSystem (NDS) has evolved beyond a traditional economy plugin. It already provides:
- Player-scoped digital values
- Global (player-related) digital values
- Server-scoped digital values
- Web-based management interface
- Web editor & admin tooling
- Long-term roadmap oriented toward a **next-generation Vault replacement**

However, many of these features were designed **before the NDS Protocol was formally defined**.

This plan exists to:
- Migrate legacy mechanisms into the **new NDS Protocol architecture**
- Preserve compatibility where strategically necessary (e.g., Vault)
- Ensure NDS becomes a **protocol-first system**, not a feature-first plugin

### 1.2 Core Goal

> Transform NDS from a powerful economy plugin into a **protocol-defined digital value infrastructure**, while allowing controlled coexistence with legacy systems.

---

## 2. Strategic Design Principles (Non-Negotiable)

These principles govern **all refactoring and future development**.

1. **Protocol First**
   - All logic must flow through the NDS Protocol layer
   - No feature may bypass protocol rules

2. **Single Source of Truth**
   - All digital values are authoritative inside NDS Core
   - No external plugin owns balance state

3. **Async-First Architecture**
   - All reads/writes are asynchronous
   - No synchronous wrappers except controlled compatibility layers

4. **Result-Driven Design**
   - Operations return results, not events
   - No event-driven state mutation

5. **Compatibility Without Compromise**
   - Legacy support exists only as an **adapter**, never as a core dependency

---

## 3. System Layering (Target Architecture)

```
┌─────────────────────────────┐
│ External Plugins / Services │
└──────────────▲──────────────┘
               │
┌──────────────┴──────────────┐
│   Compatibility Layer       │  ← Vault, Legacy APIs
│   (Adapters / Bridges)      │
└──────────────▲──────────────┘
               │
┌──────────────┴──────────────┐
│      NDS Protocol API       │  ← Public Contract
└──────────────▲──────────────┘
               │
┌──────────────┴──────────────┐
│        NDS Core Engine      │  ← Authority & Rules
│  (Storage, Atomicity, ACL)  │
└──────────────▲──────────────┘
               │
┌──────────────┴──────────────┐
│      Storage / Web / DB     │
└─────────────────────────────┘
```

---

## 4. Legacy Feature Classification

### 4.1 Features to Fully Migrate (Protocol-Native)

These **must be rewritten or refactored** to use the new protocol:

- Player personal digital values
- Global (player-linked) digital values
- Server-scoped digital values
- Permission-controlled balance access
- Atomic transfer logic
- Web editor value mutation

Rule:
> No legacy direct DB access is allowed once migrated.

---

### 4.2 Features to Keep as Compatibility Adapters

These features remain, but **only as bridges**:

- Vault Economy API
- Legacy synchronous getters/setters
- Old plugin-facing APIs

Constraints:
- Must internally call async NDS Protocol
- Must NOT expose new capabilities
- May be removed in future major versions

---

## 5. Vault Compatibility Strategy

### 5.1 Positioning

Vault is treated as:
> A **compatibility interface**, not a first-class citizen.

### 5.2 Implementation Rules

- Vault methods call NDS async protocol internally
- Blocking calls are isolated and minimal
- Precision loss is explicitly documented
- Vault operations are **best-effort**, not guaranteed optimal

### 5.3 Communication to Developers

- Vault support is labeled as **Legacy / Compatibility Mode**
- NDS-native plugins are strongly recommended

---

## 6. Web Interface Refactor Plan

### 6.1 Current State

- Web UI directly edits values
- Business rules partially duplicated

### 6.2 Target State

- Web UI becomes a **client of NDS Protocol**
- All edits go through protocol validation
- Same permission & atomicity rules as in-game

### 6.3 Editor Constraints

- No raw value overwrite without protocol permission
- Audit logging enforced at protocol layer

---

## 7. Migration Phases

### Phase 1 – Protocol Lock-In (Foundation)

- Freeze legacy feature expansion
- Enforce protocol usage in new code
- Introduce internal migration flags

Deliverables:
- Protocol API stable
- Legacy warnings logged

---

### Phase 2 – Core Feature Migration

- Refactor player / global / server values
- Remove direct DB access
- Ensure atomic operations

Deliverables:
- 100% value logic through protocol
- Backward compatibility preserved

---

### Phase 3 – Adapter Isolation

- Encapsulate Vault & legacy APIs
- Add deprecation notices
- Introduce metrics (who still uses legacy)

Deliverables:
- Clear boundary between protocol and adapters

---

### Phase 4 – NDS-Native Expansion

- Encourage third-party NDS-native plugins
- Provide documentation & examples
- Optional certification tooling

Deliverables:
- Ecosystem shift toward protocol usage

---

## 8. Engineering Guidelines for the Team

### 8.1 Mandatory Rules

- No feature may bypass protocol
- No new synchronous APIs unless approved
- No event-based economy logic

### 8.2 Code Review Checklist

- Does this code call the protocol?
- Is state mutated atomically?
- Is async handled correctly?
- Is this feature legacy or native?

---

## 9. Risk Management

### Identified Risks

- Developer resistance to async APIs
- Vault-dependent plugins expecting sync behavior
- Increased architectural complexity

### Mitigation

- Clear documentation
- Strong defaults
- Compatibility adapters

---

## 10. Long-Term Outlook

NDS is positioned to become:

- A protocol-level replacement for Vault
- A unified digital value infrastructure
- A cross-platform economy standard

Vault compatibility exists to **ease the transition**, not to define the future.

> The protocol is the product.

---

## 11. Protocol Migration Practical Guide (Required Reading for Developers)

### 11.1 Mandatory Type Conversion Specification

**⚠️ Critical Rule: All balance operations MUST use `BigDecimal`**

#### Problem Description

Legacy Vault API or direct database operations may use `double`, `float`, or `int`, but the NDS protocol layer **strictly requires** `BigDecimal`.

#### Migration Steps

**Step 1: Identify all numeric operations**

```java
// ❌ Legacy code (incorrect)
double balance = economy.getBalance(player);
economy.withdrawPlayer(player, 100.5);

// ✅ New code (correct)
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data(); // BigDecimal, not double
        }
    });
```

**Step 2: Complete conversion at the adapter layer**

If integrating from legacy Vault, conversion must occur immediately at the adapter layer:

```java
// Vault adapter example
public class VaultAdapter {
    public CompletableFuture<NdsResult<BigDecimal>> getBalance(Player player) {
        // Vault returns double; convert to BigDecimal immediately
        double vaultBalance = economy.getBalance(player);
        BigDecimal ndsBalance = BigDecimal.valueOf(vaultBalance);
        
        // Use NDS protocol
        AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
        NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
        return runtime.query().queryBalance(assetId, identity);
    }
}
```

**Step 3: Strictly prohibit primitive type parameters**

```java
// ❌ Absolutely forbidden
transaction.delta(100.5);  // double, compilation error
transaction.delta(100);    // int, compilation error

// ✅ Must use BigDecimal
transaction.delta(BigDecimal.valueOf(100.5));
transaction.delta(new BigDecimal("100.5"));
```

---

### 11.2 Async Result Handling: From "Event Listening" to "Result Listening"

#### Core Change

**Legacy mechanism (event-driven):**

```java
// ❌ Legacy approach: listening to Bukkit events to intercept transactions
@EventHandler
public void onTransaction(EconomyTransactionEvent event) {
    if (event.getAmount() > 1000) {
        event.setCancelled(true); // Cancel transaction
    }
}
```

**New mechanism (result-driven):**

```java
// ✅ New approach: call protocol methods and handle returned results
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .build();

runtime.eventBus().publish(transaction)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            // Transaction succeeded
            player.sendMessage("Transaction successful");
        } else {
            // Transaction failed (insufficient balance, permission check, etc.)
            NdsError error = result.error();
            player.sendMessage("Transaction failed: " + error.message());
        }
    }, runtime.defaultExecutor());
```

#### Key Differences

1. **Location of interception logic changes**
   - **Legacy mechanism**: cancel or modify in event listeners
   - **New mechanism**: complete in the Core Engine filter chain
   - **Impact**: All permission checks, balance validation, and business rules are uniformly processed at the protocol layer

2. **Event cancellation is no longer supported**
   - NDS protocol **does not support** canceling transactions through event listeners
   - All validation must be completed before publishing, or failure is expressed through `NdsResult`

3. **Migration recommendations**

   ```java
   // Legacy code: event listener
   @EventHandler
   public void onTransaction(EconomyTransactionEvent event) {
       // Business logic
   }
   
   // New code: validate before publishing or handle results
   public void processTransaction(Player player, BigDecimal amount) {
       // Approach 1: validate before publishing
       runtime.query().queryBalance(assetId, identity)
           .thenAcceptAsync(balanceResult -> {
               if (balanceResult.isSuccess() && balanceResult.data().compareTo(amount) >= 0) {
                   // Sufficient balance, publish transaction
                   publishTransaction(amount);
               } else {
                   player.sendMessage("Insufficient balance");
               }
           }, runtime.defaultExecutor());
       
       // Approach 2: publish directly and handle results
       publishTransaction(amount)
           .thenAcceptAsync(result -> {
               if (!result.isSuccess()) {
                   // Handle failure (may be insufficient balance, permission issue, etc.)
                   handleFailure(result.error());
               }
           }, runtime.defaultExecutor());
   }
   ```

---

### 11.3 Namespace & Identifiers

#### AssetId Format Specification

NDS protocol adopts the `scope:name` format, similar to Minecraft's native NamespaceKey pattern.

**Format description:**

```
scope:name
```

- **scope**: Scope (`PLAYER`, `SERVER`, `GLOBAL`)
- **name**: Asset name (lowercase letters, numbers, underscores)

**Examples:**

```java
// ✅ Correct format
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
// fullId() = "player:coins"

AssetId bossHp = AssetId.of(AssetScope.SERVER, "world_boss_hp");
// fullId() = "server:world_boss_hp"

// Parse from string
AssetId fromString = AssetId.fromString("player:coins");
```

#### Migration Steps

**Step 1: Map legacy asset names to new format**

```java
// Legacy code: using strings directly
String digitalName = "coins";
getPlayerDigitalAmount(playerUUID, digitalName);

// New code: using AssetId
AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
runtime.query().queryBalance(assetId, identity);
```

**Step 2: Resolve naming conflicts**

Legacy plugins may have multiple plugins using the same "currency name", causing conflicts. The new protocol resolves this through `scope:name`:

```java
// Plugin A's currency
AssetId pluginACoins = AssetId.of(AssetScope.PLAYER, "plugin_a_coins");

// Plugin B's currency
AssetId pluginBCoins = AssetId.of(AssetScope.PLAYER, "plugin_b_coins");

// No conflict because they are independent AssetIds
```

**Step 3: Migrate global assets**

```java
// Legacy code: global values
getServerDigitalAmount("world_boss_hp");

// New code: server-scoped assets
AssetId bossHp = AssetId.of(AssetScope.SERVER, "world_boss_hp");
NdsIdentity serverIdentity = null; // Server assets do not require identity
runtime.query().queryBalance(bossHp, serverIdentity);
```

---

### 11.4 Vault Adapter Layer "Side Effect" Handling

#### Risk Description

Because NDS's underlying architecture is **fully asynchronous**, when legacy plugins call Vault API on the main thread, the adapter layer will attempt to use `join()` or `.get()` to block and wait.

**Potential issues:**
- May cause thread blocking in **Folia** (multi-threaded server) environments
- May affect main thread performance under high load
- Blocking time depends on database response speed

#### Adapter Layer Implementation Example

```java
// Vault adapter layer (simplified example)
public class VaultEconomyAdapter implements Economy {
    
    @Override
    public double getBalance(Player player) {
        // ⚠️ Warning: This is a blocking call
        AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
        NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
        
        try {
            // Block waiting for result (not recommended, but Vault API requires synchronous)
            CompletableFuture<NdsResult<BigDecimal>> future = 
                runtime.query().queryBalance(assetId, identity);
            
            NdsResult<BigDecimal> result = future.get(5, TimeUnit.SECONDS); // 5-second timeout
            
            if (result.isSuccess()) {
                return result.data().doubleValue(); // Precision loss (documented)
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            getLogger().warning("Vault getBalance failed: " + e.getMessage());
            return 0.0;
        }
    }
}
```

#### Migration Recommendations

**Phase 1: Identify Vault calls**

```java
// Search in legacy plugins
economy.getBalance(...)
economy.withdrawPlayer(...)
economy.depositPlayer(...)
```

**Phase 2: Gradually replace with NDS native API**

```java
// ❌ Legacy code (Vault)
double balance = economy.getBalance(player);
economy.withdrawPlayer(player, 100.0);

// ✅ New code (NDS native)
AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());

// Query balance
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // Use balance
        }
    }, runtime.defaultExecutor());

// Deduct amount
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(-100))
    .consistency(ConsistencyMode.STRONG)
    .reason("purchase")
    .build();

runtime.eventBus().publish(transaction)
    .thenAcceptAsync(publishResult -> {
        if (publishResult.isSuccess()) {
            // Deduction successful
        }
    }, runtime.defaultExecutor());
```

**Phase 3: Monitoring and optimization**
- Monitor Vault call frequency
- Prioritize migration of high-frequency call plugins
- Pay special attention to performance impact in Folia environments

---

### 11.5 Data Audit & Provenance (Audit Log) Integration

#### Core Concept

Every change made through the protocol can have an attached **Reason** and **Metadata**, enabling the Web management interface to correctly display transaction sources.

#### Using Reason

**Purpose:** Identify the reason for the transaction, used for auditing and display.

```java
// ✅ Correct: provide a clear reason
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("SHOP_PURCHASE")  // Clearly identified as shop purchase
    .build();

// ❌ Incorrect: missing reason or using vague description
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("transaction")  // Too vague, cannot distinguish transaction type
    .build();
```

#### Reason Naming Convention

It is recommended to use uppercase letters and underscores, similar to enum constants:

```java
// ✅ Recommended format
.reason("SHOP_PURCHASE")
.reason("QUEST_REWARD")
.reason("ADMIN_SET")
.reason("PLAYER_TRANSFER")
.reason("AUTOMATED_REFUND")

// ❌ Not recommended
.reason("shop purchase")  // lowercase with spaces
.reason("購買")  // non-English (although supported, not conducive to internationalization)
```

#### Using Metadata

**Purpose:** Attach additional context information without affecting business logic.

```java
// Create metadata
Map<String, String> metadata = new HashMap<>();
metadata.put("shopId", "diamond_shop_001");
metadata.put("itemId", "diamond_sword");
metadata.put("adminOperator", "admin_player_name");

NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("SHOP_PURCHASE")
    .metadata(metadata)  // Attach metadata
    .build();
```

#### Migration Steps

**Step 1: Identify existing transaction logic**

```java
// Legacy code: may not have reason
givePlayerDigital(playerUUID, "coins", amount);

// New code: must provide reason
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(amount)
    .consistency(ConsistencyMode.STRONG)
    .reason("QUEST_REWARD")  // Clearly identified
    .build();
```

**Step 2: Establish Reason constants class**

```java
public class TransactionReasons {
    public static final String SHOP_PURCHASE = "SHOP_PURCHASE";
    public static final String QUEST_REWARD = "QUEST_REWARD";
    public static final String ADMIN_SET = "ADMIN_SET";
    public static final String PLAYER_TRANSFER = "PLAYER_TRANSFER";
    // ...
}
```

**Step 3: Display in Web management interface**

The Web interface can display more user-friendly transaction records based on `reason` and `metadata`:

```
Transaction Record:
- Time: 2024-01-15 10:30:00
- Type: Shop Purchase (SHOP_PURCHASE)
- Shop: diamond_shop_001
- Item: diamond_sword
- Amount: -100 coins
```

---

### 11.6 Migration Checklist

Before starting migration, please confirm:

- [ ] All numeric operations have been converted to `BigDecimal`
- [ ] All synchronous calls have been changed to async callbacks (`.thenAccept()`, etc.)
- [ ] All `NdsResult` checks `isSuccess()`
- [ ] All Bukkit API calls use `defaultExecutor()` or `mainThreadExecutor()`
- [ ] All asset names have been mapped to `AssetId` (`scope:name` format)
- [ ] All transactions provide a clear `reason`
- [ ] All Vault calls have been identified and planned for migration
- [ ] All event listener logic has been transferred to result handling

---

## 12. Final Statement

This migration is not a refactor for cleanliness.

It is a **strategic evolution**:
- From plugin → platform
- From feature → protocol
- From convenience → correctness

All future decisions must reinforce this direction.

---

**Version**: 2.1.0  
**Last Updated**: 2026-01-27  
**Status**: ✅ Stable
