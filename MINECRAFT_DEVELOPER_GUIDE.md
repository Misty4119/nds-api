# NDS API v2.0 Minecraft Developer Guide

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *The successor of Vault, not a replacement — a protocol layer.*

---

## 📘 About This Guide

This guide is designed specifically for **Minecraft plugin developers**, covering how to use NDS API in Spigot/Paper/Folia environments.

For general API concepts and multi-language support, please refer to [DEVELOPER_GUIDE.md](./DEVELOPER_GUIDE.md).

---

## 🔥 What Is an NDS-Native Plugin?

### NDS-Native Plugin Definition

**An NDS-native plugin must satisfy ALL of the following conditions:**

✅ **Must Do:**
- ✅ Use `NdsProvider.get()` to obtain `NdsRuntime` (the only entry point)
- ✅ All state comes from NDS (do not store any economic/state data)
- ✅ All behavior is driven by "result callbacks" (async-first)
- ✅ Use `NdsResult` for error handling (check `isSuccess()` before accessing `.data()`)
- ✅ Use `NdsTransactionBuilder` to create transactions
- ✅ Use `runtime.defaultExecutor()` (recommended) or `runtime.mainThreadExecutor()` (backward compatible) when calling Bukkit API in callbacks
- ✅ Properly handle `NdsResult` failures with `.onFailure()` or `.exceptionally()`
- ✅ Use `BigDecimal` for all economic values

❌ **Absolutely Forbidden:**
- ❌ Do not use `.get()` on `CompletableFuture` (blocks main thread)
- ❌ Do not use `double`, `float`, or `int` for economic values
- ❌ Do not store any economic/state data locally
- ❌ Do not cache balances or asset values
- ❌ Do not put Bukkit/JVM objects (Entity, Location, ItemStack) in `NdsPayload`
- ❌ New plugins must not use the deprecated `NoieDigitalSystemAPI`
- ❌ Do not call Bukkit API directly in async callbacks
- ❌ Do not access `.data()` on a failed `NdsResult` (check `isSuccess()` first)
- ❌ Do not modify state directly (only through events)

---

## ⚠️ Consequences of Violations

**Non-compliant plugins** (violating "Must Do" or "Absolutely Forbidden"):
- Will not be listed in official recommendations
- No future version compatibility guarantee
- Cannot use new NDS features or optimizations

**Violating core principles**:
- No guarantee of correct behavior (may cause data loss, inconsistency)
- State may desynchronize in cross-server environments

---

## 📦 Dependency Setup

### plugin.yml

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.myplugin.MyPlugin
depend: [NoieDigitalSystem]  # Must depend on NDS
api-version: '1.21'
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // Use compileOnly because NDS provides this API at runtime
    compileOnly("io.github.misty4119:noiedigitalsystem-api:2.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.misty4119</groupId>
    <artifactId>noiedigitalsystem-api</artifactId>
    <version>2.1.0</version>
    <scope>provided</scope>
</dependency>
```

---

## 🚀 Quick Start

### Get Runtime Instance

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

public class MyPlugin extends JavaPlugin {
    private NdsRuntime runtime;
    
    @Override
    public void onEnable() {
        // Check if NDS is initialized
        if (!NdsProvider.isInitialized()) {
            getLogger().severe("NDS not initialized!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        runtime = NdsProvider.get();
        getLogger().info("Successfully connected to NDS");
    }
}
```

### Create Player Identity

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

// Create identity from player UUID
NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());

// Or explicitly specify type
NdsIdentity playerIdentity = NdsIdentity.of(
    player.getUniqueId().toString(), 
    IdentityType.PLAYER
);
```

### Create Asset ID

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

// Player asset
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");

// Or use string format
AssetId coins = AssetId.fromString("player:coins");
```

### Query Player Balance

```java
import java.math.BigDecimal;

public void checkBalance(Player player) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    runtime.query().queryBalance(coinsAssetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                BigDecimal balance = result.data();
                player.sendMessage("§aYour balance: §e" + balance + " §acoins");
            } else {
                player.sendMessage("§cFailed to query balance: " + result.error().message());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("Error querying balance: " + ex.getMessage());
            player.sendMessage("§cSystem error, please try again later");
            return null;
        });
}
```

---

## 🎮 Minecraft-Specific Patterns

### Pattern 1: Shop Purchase (Atomic Transaction)

```java
public void purchaseItem(Player player, String itemId, BigDecimal price) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // Create transaction (negative delta = deduction)
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(identity)
        .asset(coinsAssetId)
        .delta(price.negate())  // Negative = subtract
        .consistency(ConsistencyMode.STRONG)
        .reason("purchase:" + itemId)
        .build();
    
    // Publish transaction
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // Transaction succeeded, give item
                ItemStack item = createItem(itemId);
                player.getInventory().addItem(item);
                player.sendMessage("§aPurchase successful!");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else {
                // Transaction failed (e.g., insufficient balance)
                player.sendMessage("§cPurchase failed: " + result.error().message());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("Error during purchase: " + ex.getMessage());
            player.sendMessage("§cPurchase failed, please try again later");
            return null;
        });
}
```

### Pattern 2: Reward Player

```java
public void rewardPlayer(Player player, BigDecimal amount, String reason) {
    NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
    NdsIdentity systemIdentity = NdsIdentity.of("system", IdentityType.SYSTEM);
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // Create transaction (positive delta = addition)
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(systemIdentity)  // System as actor
        .asset(coinsAssetId)
        .delta(amount)  // Positive = add
        .consistency(ConsistencyMode.STRONG)
        .target(playerIdentity)  // Target player
        .reason(reason)
        .build();
    
    // Publish transaction
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                player.sendMessage("§aYou received §e" + amount + " §acoins!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("Error rewarding player: " + ex.getMessage());
            return null;
        });
}
```

### Pattern 3: Player-to-Player Transfer

```java
public void transfer(Player from, Player to, BigDecimal amount) {
    NdsIdentity fromIdentity = NdsIdentity.fromString(from.getUniqueId().toString());
    NdsIdentity toIdentity = NdsIdentity.fromString(to.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // Create transfer transaction
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(fromIdentity)
        .asset(coinsAssetId)
        .delta(amount.negate())  // Deduct from sender
        .consistency(ConsistencyMode.STRONG)
        .source(fromIdentity)
        .target(toIdentity)
        .reason("transfer")
        .build();
    
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // Sender deducted successfully, now add to receiver
                NdsTransaction receiveTransaction = NdsTransactionBuilder.create()
                    .actor(toIdentity)
                    .asset(coinsAssetId)
                    .delta(amount)  // Add to receiver
                    .consistency(ConsistencyMode.STRONG)
                    .source(fromIdentity)
                    .target(toIdentity)
                    .reason("transfer")
                    .build();
                
                runtime.eventBus().publish(receiveTransaction)
                    .thenAcceptAsync(receiveResult -> {
                        if (receiveResult.isSuccess()) {
                            from.sendMessage("§aSuccessfully transferred §e" + amount + " §acoins to §e" + to.getName());
                            to.sendMessage("§aYou received §e" + amount + " §acoins from §e" + from.getName());
                        }
                    }, runtime.defaultExecutor());
            } else {
                from.sendMessage("§cTransfer failed: " + result.error().message());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("Error during transfer: " + ex.getMessage());
            from.sendMessage("§cTransfer failed, please try again later");
            return null;
        });
}
```

### Pattern 4: Quest Completion Reward

```java
public void onQuestComplete(Player player, String questId, BigDecimal reward) {
    NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
    NdsIdentity systemIdentity = NdsIdentity.of("quest-system", IdentityType.SYSTEM);
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // Create quest reward payload
    NdsPayload payload = NdsPayload.builder()
        .put("questId", questId)
        .put("reward", reward)
        .put("completedAt", System.currentTimeMillis())
        .build();
    
    // Create transaction
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(systemIdentity)
        .asset(coinsAssetId)
        .delta(reward)
        .consistency(ConsistencyMode.STRONG)
        .target(playerIdentity)
        .reason("quest:" + questId)
        .payload(payload)
        .build();
    
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // Display completion message
                player.sendTitle(
                    "§6Quest Complete!",
                    "§e+" + reward + " Coins",
                    10, 70, 20
                );
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor());
}
```

---

## ⚠️ Minecraft-Specific Considerations

### 1. Thread Safety

**Bukkit API is not thread-safe.** All Bukkit operations must be executed on the main thread.

```java
// ✅ Correct: Use defaultExecutor() (recommended, added in v2.0.0)
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("Balance: " + result.data());
        }
    }, runtime.defaultExecutor());

// ✅ Correct: Use mainThreadExecutor() (backward compatible)
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("Balance: " + result.data());
        }
    }, runtime.mainThreadExecutor());

// ❌ Wrong: Calling Bukkit API directly in async thread
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        player.sendMessage("Balance: " + result.data()); // May throw exception!
    });
```

### 2. Payload Restrictions

**`NdsPayload` can only contain serializable primitive types.** Bukkit objects are forbidden.

```java
// ❌ Wrong: Putting Bukkit objects
NdsPayload payload = NdsPayload.builder()
    .put("item", itemStack)      // Illegal!
    .put("location", location)   // Illegal!
    .put("player", player)       // Illegal!
    .build();

// ✅ Correct: Use primitive types
NdsPayload payload = NdsPayload.builder()
    .put("itemType", itemStack.getType().name())
    .put("itemAmount", itemStack.getAmount())
    .put("world", location.getWorld().getName())
    .put("x", location.getX())
    .put("y", location.getY())
    .put("z", location.getZ())
    .put("playerUuid", player.getUniqueId().toString())
    .build();
```

### 3. Folia Compatibility

NDS API fully supports Folia. Using `runtime.defaultExecutor()` will automatically select the correct scheduler.

```java
// ✅ Works correctly in Folia environment
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        // defaultExecutor() uses GlobalRegionScheduler in Folia
        player.sendMessage("Balance: " + result.data());
    }, runtime.defaultExecutor());
```

### 4. Do Not Cache Balances

```java
// ❌ Wrong: Caching balances
private final Map<UUID, BigDecimal> balanceCache = new HashMap<>();

public BigDecimal getBalance(UUID uuid) {
    return balanceCache.get(uuid); // May be outdated!
}

// ✅ Correct: Query every time
public void getBalance(UUID uuid, Consumer<BigDecimal> callback) {
    runtime.query().queryBalance(assetId, NdsIdentity.fromString(uuid.toString()))
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                callback.accept(result.data());
            }
        }, runtime.defaultExecutor());
}
```

---

## ❌ Common Anti-Patterns

### Anti-Pattern 1: Blocking Main Thread

```java
// ❌ Wrong: Blocking main thread
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    NdsResult<BigDecimal> result = runtime.query()
        .queryBalance(assetId, identity).get(); // Blocking! Causes server lag!
    sender.sendMessage("Balance: " + result.data());
    return true;
}
```

**✅ Correct approach:**

```java
// ✅ Correct: Async handling
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    runtime.query().queryBalance(assetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                sender.sendMessage("Balance: " + result.data());
            }
        }, runtime.defaultExecutor());
    return true;
}
```

### Anti-Pattern 2: Using Legacy API

```java
// ❌ Wrong: Using deprecated API
NoieDigitalSystem nds = (NoieDigitalSystem) getServer().getPluginManager().getPlugin("NoieDigitalSystem");
NoieDigitalSystemAPI api = nds.getAPI();

// ✅ Correct: Use NdsProvider
NdsRuntime runtime = NdsProvider.get();
```

### Anti-Pattern 3: Storing Economic Data Yourself

```java
// ❌ Wrong: Storing economic data yourself
public class MyPlugin extends JavaPlugin {
    private final Map<UUID, Double> playerMoney = new HashMap<>();
    
    public void giveMoney(UUID uuid, double amount) {
        playerMoney.merge(uuid, amount, Double::sum); // Won't sync to other servers!
    }
}
```

---

## 🏗️ Architecture Diagram

```
Your Minecraft Plugin
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← Protocol Layer
│   (Interfaces Only)  │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Bukkit        │  ← Minecraft Implementation
│   (Spigot/Paper/    │
│    Folia Support)   │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← Core Logic
│   (Event Store,     │
│    Projections)     │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(Event Store)  (Cross-Server Sync)
```

---

## 🔄 Migration from v1.0

### Key Changes

| v1.0 | v2.0 |
|------|------|
| `NoieDigitalSystem.getAPI()` | `NdsProvider.get()` |
| `CompletableFuture<Boolean>` | `CompletableFuture<NdsResult<T>>` |
| Direct API calls | `NdsTransactionBuilder` |
| `UUID` | `NdsIdentity` |
| String names | `AssetId` |

### Migration Example

```java
// v1.0 (deprecated)
NoieDigitalSystemAPI api = ((NoieDigitalSystem) plugin).getAPI();
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            player.sendMessage("Success");
        }
    });

// v2.0 (recommended)
NdsRuntime runtime = NdsProvider.get();
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(NdsIdentity.of("system", IdentityType.SYSTEM))
    .asset(AssetId.of(AssetScope.PLAYER, "coins"))
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .target(NdsIdentity.fromString(uuid.toString()))
    .build();

runtime.eventBus().publish(transaction)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("Success");
        }
    }, runtime.defaultExecutor());
```

---

## 📖 Additional Resources

- **General Developer Guide**: [DEVELOPER_GUIDE.md](./DEVELOPER_GUIDE.md)
- **Traditional Chinese Minecraft Guide**: [MINECRAFT_DEVELOPER_GUIDE_TW.md](./MINECRAFT_DEVELOPER_GUIDE_TW.md)
- **AI Development Context**: [AGENTS.md](./AGENTS.md)

---

**Version**: 2.1.0  
**Project Inception**: 2025-12-22  
**Last Updated**: 2026-01-27  
**Status**: ✅ Stable
