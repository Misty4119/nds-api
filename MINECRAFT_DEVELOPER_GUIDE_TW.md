# NDS API v2.0 Minecraft 開發者指南

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *Vault 的繼承者，而非替代品 — 協議層。*

---

## 📘 關於本指南

本指南專為 **Minecraft 插件開發者**設計，涵蓋 NDS API 在 Spigot/Paper/Folia 環境中的使用方式。

如需通用 API 概念與多語言支援，請參閱 [DEVELOPER_GUIDE_TW.md](./DEVELOPER_GUIDE_TW.md)。

---

## 🔥 何謂 NDS 原生插件？

### NDS 原生插件定義

**NDS 原生插件 = 必須同時滿足以下所有條件：**

✅ **必須做到：**
- ✅ 使用 `NdsProvider.get()` 取得 `NdsRuntime`（唯一入口點）
- ✅ 所有狀態來自 NDS（不儲存任何經濟/狀態資料）
- ✅ 所有行為以「結果回調」驅動（非同步優先）
- ✅ 使用 `NdsResult` 處理錯誤（存取 `.data()` 前檢查 `isSuccess()`）
- ✅ 使用 `NdsTransactionBuilder` 建立交易
- ✅ 在回調中呼叫 Bukkit API 時使用 `runtime.defaultExecutor()`（推薦）或 `runtime.mainThreadExecutor()`（向後兼容）
- ✅ 使用 `.onFailure()` 或 `.exceptionally()` 正確處理 `NdsResult` 失敗
- ✅ 所有經濟數值使用 `BigDecimal`

❌ **絕對禁止：**
- ❌ 不得在 `CompletableFuture` 上使用 `.get()`（阻塞主執行緒）
- ❌ 不得使用 `double`、`float` 或 `int` 作為經濟數值
- ❌ 不得在本地儲存任何經濟/狀態資料
- ❌ 不得快取餘額或資產數值
- ❌ 不得在 `NdsPayload` 中放入 Bukkit/JVM 物件（Entity、Location、ItemStack）
- ❌ 新插件不得使用已棄用的 `NoieDigitalSystemAPI`
- ❌ 不得在非同步回調中直接呼叫 Bukkit API
- ❌ 不得在失敗的 `NdsResult` 上存取 `.data()`（先檢查 `isSuccess()`）
- ❌ 不得直接修改狀態（僅能透過事件）

---

## ⚠️ 違規後果

**非合規插件**（違反「必須做到」或「絕對禁止」）：
- 不列入官方推薦
- 無未來版本相容性保證
- 無法使用新 NDS 功能或優化

**違反核心原則**：
- 不保證行為正確性（可能導致資料遺失、不一致）
- 跨伺服器環境中狀態可能不同步

---

## 📦 依賴設定

### plugin.yml

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.myplugin.MyPlugin
depend: [NoieDigitalSystem]  # 必須依賴 NDS
api-version: '1.21'
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // 使用 compileOnly，因為 NDS 會在執行時提供此 API
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

## 🚀 快速開始

### 取得 Runtime 實例

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

public class MyPlugin extends JavaPlugin {
    private NdsRuntime runtime;
    
    @Override
    public void onEnable() {
        // 檢查 NDS 是否已初始化
        if (!NdsProvider.isInitialized()) {
            getLogger().severe("NDS 未初始化！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        runtime = NdsProvider.get();
        getLogger().info("成功連接到 NDS");
    }
}
```

### 建立玩家身份

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

// 從玩家 UUID 建立身份
NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());

// 或明確指定類型
NdsIdentity playerIdentity = NdsIdentity.of(
    player.getUniqueId().toString(), 
    IdentityType.PLAYER
);
```

### 建立資產 ID

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

// 玩家資產
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");

// 或使用字串格式
AssetId coins = AssetId.fromString("player:coins");
```

### 查詢玩家餘額

```java
import java.math.BigDecimal;

public void checkBalance(Player player) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    runtime.query().queryBalance(coinsAssetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                BigDecimal balance = result.data();
                player.sendMessage("§a您的餘額: §e" + balance + " §a金幣");
            } else {
                player.sendMessage("§c查詢餘額失敗: " + result.error().message());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("查詢餘額時發生錯誤: " + ex.getMessage());
            player.sendMessage("§c系統錯誤，請稍後再試");
            return null;
        });
}
```

---

## 🎮 Minecraft 專屬模式

### 模式 1：商店購買（原子交易）

```java
public void purchaseItem(Player player, String itemId, BigDecimal price) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // 建立交易（負數 delta = 扣款）
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(identity)
        .asset(coinsAssetId)
        .delta(price.negate())  // 負數 = 減少
        .consistency(ConsistencyMode.STRONG)
        .reason("purchase:" + itemId)
        .build();
    
    // 發布交易
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // 交易成功，發放物品
                ItemStack item = createItem(itemId);
                player.getInventory().addItem(item);
                player.sendMessage("§a購買成功！");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else {
                // 交易失敗（例如：餘額不足）
                player.sendMessage("§c購買失敗: " + result.error().message());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("購買時發生錯誤: " + ex.getMessage());
            player.sendMessage("§c購買失敗，請稍後再試");
            return null;
        });
}
```

### 模式 2：獎勵玩家

```java
public void rewardPlayer(Player player, BigDecimal amount, String reason) {
    NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
    NdsIdentity systemIdentity = NdsIdentity.of("system", IdentityType.SYSTEM);
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // 建立交易（正數 delta = 增加）
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(systemIdentity)  // 系統作為執行者
        .asset(coinsAssetId)
        .delta(amount)  // 正數 = 增加
        .consistency(ConsistencyMode.STRONG)
        .target(playerIdentity)  // 目標玩家
        .reason(reason)
        .build();
    
    // 發布交易
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                player.sendMessage("§a您獲得了 §e" + amount + " §a金幣！");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("獎勵玩家時發生錯誤: " + ex.getMessage());
            return null;
        });
}
```

### 模式 3：玩家間轉帳

```java
public void transfer(Player from, Player to, BigDecimal amount) {
    NdsIdentity fromIdentity = NdsIdentity.fromString(from.getUniqueId().toString());
    NdsIdentity toIdentity = NdsIdentity.fromString(to.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // 建立轉帳交易
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(fromIdentity)
        .asset(coinsAssetId)
        .delta(amount.negate())  // 從發送者扣除
        .consistency(ConsistencyMode.STRONG)
        .source(fromIdentity)
        .target(toIdentity)
        .reason("transfer")
        .build();
    
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // 發送者扣款成功，現在給接收者增加餘額
                NdsTransaction receiveTransaction = NdsTransactionBuilder.create()
                    .actor(toIdentity)
                    .asset(coinsAssetId)
                    .delta(amount)  // 給接收者增加
                    .consistency(ConsistencyMode.STRONG)
                    .source(fromIdentity)
                    .target(toIdentity)
                    .reason("transfer")
                    .build();
                
                runtime.eventBus().publish(receiveTransaction)
                    .thenAcceptAsync(receiveResult -> {
                        if (receiveResult.isSuccess()) {
                            from.sendMessage("§a成功轉帳 §e" + amount + " §a金幣給 §e" + to.getName());
                            to.sendMessage("§a您收到了來自 §e" + from.getName() + " §a的 §e" + amount + " §a金幣");
                        }
                    }, runtime.defaultExecutor());
            } else {
                from.sendMessage("§c轉帳失敗: " + result.error().message());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            getLogger().severe("轉帳時發生錯誤: " + ex.getMessage());
            from.sendMessage("§c轉帳失敗，請稍後再試");
            return null;
        });
}
```

### 模式 4：任務完成獎勵

```java
public void onQuestComplete(Player player, String questId, BigDecimal reward) {
    NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
    NdsIdentity systemIdentity = NdsIdentity.of("quest-system", IdentityType.SYSTEM);
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // 建立任務獎勵 Payload
    NdsPayload payload = NdsPayload.builder()
        .put("questId", questId)
        .put("reward", reward)
        .put("completedAt", System.currentTimeMillis())
        .build();
    
    // 建立交易
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
                // 顯示完成訊息
                player.sendTitle(
                    "§6任務完成！",
                    "§e+" + reward + " 金幣",
                    10, 70, 20
                );
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }, runtime.defaultExecutor());
}
```

---

## ⚠️ Minecraft 專屬注意事項

### 1. 執行緒安全

**Bukkit API 不是執行緒安全的。** 所有 Bukkit 操作必須在主執行緒執行。

```java
// ✅ 正確：使用 defaultExecutor()（推薦，v2.0.0 新增）
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("餘額: " + result.data());
        }
    }, runtime.defaultExecutor());

// ✅ 正確：使用 mainThreadExecutor()（向後兼容）
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("餘額: " + result.data());
        }
    }, runtime.mainThreadExecutor());

// ❌ 錯誤：在非同步執行緒直接呼叫 Bukkit API
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        player.sendMessage("餘額: " + result.data()); // 可能拋出例外！
    });
```

### 2. Payload 限制

**`NdsPayload` 只能包含可序列化的基本型別。** 禁止放入 Bukkit 物件。

```java
// ❌ 錯誤：放入 Bukkit 物件
NdsPayload payload = NdsPayload.builder()
    .put("item", itemStack)      // 非法！
    .put("location", location)   // 非法！
    .put("player", player)       // 非法！
    .build();

// ✅ 正確：使用基本型別
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

### 3. Folia 相容性

NDS API 完全支援 Folia。使用 `runtime.defaultExecutor()` 會自動選擇正確的排程器。

```java
// ✅ 在 Folia 環境中也能正確運作
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        // defaultExecutor() 在 Folia 中會使用 GlobalRegionScheduler
        player.sendMessage("餘額: " + result.data());
    }, runtime.defaultExecutor());
```

### 4. 不要快取餘額

```java
// ❌ 錯誤：快取餘額
private final Map<UUID, BigDecimal> balanceCache = new HashMap<>();

public BigDecimal getBalance(UUID uuid) {
    return balanceCache.get(uuid); // 可能已過時！
}

// ✅ 正確：每次查詢
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

## ❌ 常見反模式

### 反模式 1：阻塞主執行緒

```java
// ❌ 錯誤：阻塞主執行緒
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    NdsResult<BigDecimal> result = runtime.query()
        .queryBalance(assetId, identity).get(); // 阻塞！會導致伺服器卡頓！
    sender.sendMessage("餘額: " + result.data());
    return true;
}
```

**✅ 正確做法**：

```java
// ✅ 正確：非同步處理
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    runtime.query().queryBalance(assetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                sender.sendMessage("餘額: " + result.data());
            }
        }, runtime.defaultExecutor());
    return true;
}
```

### 反模式 2：使用舊版 API

```java
// ❌ 錯誤：使用已棄用的 API
NoieDigitalSystem nds = (NoieDigitalSystem) getServer().getPluginManager().getPlugin("NoieDigitalSystem");
NoieDigitalSystemAPI api = nds.getAPI();

// ✅ 正確：使用 NdsProvider
NdsRuntime runtime = NdsProvider.get();
```

### 反模式 3：自行儲存經濟資料

```java
// ❌ 錯誤：自行儲存經濟資料
public class MyPlugin extends JavaPlugin {
    private final Map<UUID, Double> playerMoney = new HashMap<>();
    
    public void giveMoney(UUID uuid, double amount) {
        playerMoney.merge(uuid, amount, Double::sum); // 不會同步到其他伺服器！
    }
}
```

---

## 🏗️ 架構圖

```
您的 Minecraft 插件
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← 協議層
│   (僅介面)            │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Bukkit        │  ← Minecraft 實作
│   (Spigot/Paper/    │
│    Folia 支援)       │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← 核心邏輯
│   (事件儲存, 投影)    │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(事件儲存)  (跨伺服器同步)
```

---

## 🔄 從 v1.0 遷移

### 關鍵變更

| v1.0 | v2.0 |
|------|------|
| `NoieDigitalSystem.getAPI()` | `NdsProvider.get()` |
| `CompletableFuture<Boolean>` | `CompletableFuture<NdsResult<T>>` |
| 直接 API 呼叫 | `NdsTransactionBuilder` |
| `UUID` | `NdsIdentity` |
| 字串名稱 | `AssetId` |

### 遷移範例

```java
// v1.0（已棄用）
NoieDigitalSystemAPI api = ((NoieDigitalSystem) plugin).getAPI();
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            player.sendMessage("成功");
        }
    });

// v2.0（推薦）
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
            player.sendMessage("成功");
        }
    }, runtime.defaultExecutor());
```

---

## 📖 其他資源

- **通用開發指南**：[DEVELOPER_GUIDE_TW.md](./DEVELOPER_GUIDE_TW.md)
- **英文版 Minecraft 指南**：[MINECRAFT_DEVELOPER_GUIDE.md](./MINECRAFT_DEVELOPER_GUIDE.md)
- **AI 開發上下文**：[AGENTS.md](./AGENTS.md)

---

**版本**：2.1.0  
**專案成立**：2025-12-22  
**最後更新**：2026-01-27  
**狀態**：✅ 穩定
