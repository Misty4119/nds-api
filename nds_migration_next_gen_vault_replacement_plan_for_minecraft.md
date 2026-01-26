# NDS Migration & Next‑Generation Economy Protocol Plan

## 1. Purpose & Vision

### 1.1 Background
NoieDigitalSystem (NDS) has evolved beyond a traditional economy plugin. It already provides:
- Player‑scoped digital values
- Global (player‑related) digital values
- Server‑scoped digital values
- Web‑based management interface
- Web editor & admin tooling
- Long‑term roadmap oriented toward a **next‑generation Vault replacement**

However, many of these features were designed **before the NDS Protocol was formally defined**.

This plan exists to:
- Migrate legacy mechanisms into the **new NDS Protocol architecture**
- Preserve compatibility where strategically necessary (e.g. Vault)
- Ensure NDS becomes a **protocol‑first system**, not a feature‑first plugin

### 1.2 Core Goal
> Transform NDS from a powerful economy plugin into a **protocol‑defined digital value infrastructure**, while allowing controlled coexistence with legacy systems.

---

## 2. Strategic Design Principles (Non‑Negotiable)

These principles govern **all refactoring and future development**.

1. **Protocol First**
   - All logic must flow through the NDS Protocol layer
   - No feature may bypass protocol rules

2. **Single Source of Truth**
   - All digital values are authoritative inside NDS Core
   - No external plugin owns balance state

3. **Async‑First Architecture**
   - All reads/writes are asynchronous
   - No synchronous wrappers except controlled compatibility layers

4. **Result‑Driven Design**
   - Operations return results, not events
   - No event‑driven state mutation

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
│  (Storage, Atomicity, ACL) │
└──────────────▲──────────────┘
               │
┌──────────────┴──────────────┐
│      Storage / Web / DB     │
└─────────────────────────────┘
```

---

## 4. Legacy Feature Classification

### 4.1 Features to Fully Migrate (Protocol‑Native)

These **must be rewritten or refactored** to use the new protocol:

- Player personal digital values
- Global (player‑linked) digital values
- Server‑scoped digital values
- Permission‑controlled balance access
- Atomic transfer logic
- Web editor value mutation

Rule:
> No legacy direct DB access is allowed once migrated.

---

### 4.2 Features to Keep as Compatibility Adapters

These features remain, but **only as bridges**:

- Vault Economy API
- Legacy synchronous getters/setters
- Old plugin‑facing APIs

Constraints:
- Must internally call async NDS Protocol
- Must NOT expose new capabilities
- May be removed in future major versions

---

## 5. Vault Compatibility Strategy

### 5.1 Positioning

Vault is treated as:
> A **compatibility interface**, not a first‑class citizen.

### 5.2 Implementation Rules

- Vault methods call NDS async protocol internally
- Blocking calls are isolated and minimal
- Precision loss is explicitly documented
- Vault operations are **best‑effort**, not guaranteed optimal

### 5.3 Communication to Developers

- Vault support is labeled as **Legacy / Compatibility Mode**
- NDS‑native plugins are strongly recommended

---

## 6. Web Interface Refactor Plan

### 6.1 Current State

- Web UI directly edits values
- Business rules partially duplicated

### 6.2 Target State

- Web UI becomes a **client of NDS Protocol**
- All edits go through protocol validation
- Same permission & atomicity rules as in‑game

### 6.3 Editor Constraints

- No raw value overwrite without protocol permission
- Audit logging enforced at protocol layer

---

## 7. Migration Phases

### Phase 1 – Protocol Lock‑In (Foundation)

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

### Phase 4 – NDS‑Native Expansion

- Encourage third‑party NDS‑native plugins
- Provide documentation & examples
- Optional certification tooling

Deliverables:
- Ecosystem shift toward protocol usage

---

## 8. Engineering Guidelines for the Team

### 8.1 Mandatory Rules

- No feature may bypass protocol
- No new synchronous APIs unless approved
- No event‑based economy logic

### 8.2 Code Review Checklist

- Does this code call the protocol?
- Is state mutated atomically?
- Is async handled correctly?
- Is this feature legacy or native?

---

## 9. Risk Management

### Identified Risks

- Developer resistance to async APIs
- Vault‑dependent plugins expecting sync behavior
- Increased architectural complexity

### Mitigation

- Clear documentation
- Strong defaults
- Compatibility adapters

---

## 10. Long‑Term Outlook

NDS is positioned to become:

- A protocol‑level replacement for Vault
- A unified digital value infrastructure
- A cross‑platform economy standard

Vault compatibility exists to **ease the transition**, not to define the future.

> The protocol is the product.

---

## 11. 協議遷移實務指南（開發者必讀）

### 11.1 數值類型的強制轉換規範

**⚠️ 關鍵規則：所有涉及餘額的操作必須使用 `BigDecimal`**

#### 問題說明
舊版 Vault API 或直接資料庫操作可能使用 `double`、`float` 或 `int`，但 NDS 協議層**嚴格要求**使用 `BigDecimal`。

#### 遷移步驟

**步驟 1：識別所有數值操作**
```java
// ❌ 舊代碼（錯誤）
double balance = economy.getBalance(player);
economy.withdrawPlayer(player, 100.5);

// ✅ 新代碼（正確）
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data(); // BigDecimal，不是 double
        }
    });
```

**步驟 2：在適配器層完成轉換**
如果從舊版 Vault 接入，必須在適配器層立即完成轉換：

```java
// Vault 適配器示例
public class VaultAdapter {
    public CompletableFuture<NdsResult<BigDecimal>> getBalance(Player player) {
        // Vault 返回 double，立即轉換為 BigDecimal
        double vaultBalance = economy.getBalance(player);
        BigDecimal ndsBalance = BigDecimal.valueOf(vaultBalance);
        
        // 使用 NDS 協議
        AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
        NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
        return runtime.query().queryBalance(assetId, identity);
    }
}
```

**步驟 3：嚴禁直接傳入原始類型**
```java
// ❌ 絕對禁止
transaction.delta(100.5);  // double，編譯錯誤
transaction.delta(100);    // int，編譯錯誤

// ✅ 必須使用 BigDecimal
transaction.delta(BigDecimal.valueOf(100.5));
transaction.delta(new BigDecimal("100.5"));
```

---

### 11.2 異步結果處理：從「監聽事件」到「監聽結果」

#### 核心變化

**舊機制（事件驅動）：**
```java
// ❌ 舊方式：監聽 Bukkit 事件來攔截交易
@EventHandler
public void onTransaction(EconomyTransactionEvent event) {
    if (event.getAmount() > 1000) {
        event.setCancelled(true); // 取消交易
    }
}
```

**新機制（結果驅動）：**
```java
// ✅ 新方式：調用協議方法，處理返回的結果
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .build();

runtime.eventBus().publish(transaction)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            // 交易成功
            player.sendMessage("交易成功");
        } else {
            // 交易失敗（餘額不足、權限檢查等）
            NdsError error = result.error();
            player.sendMessage("交易失敗: " + error.message());
        }
    }, runtime.mainThreadExecutor());
```

#### 關鍵差異

1. **攔截邏輯位置改變**
   - **舊機制**：在事件監聽器中取消或修改
   - **新機制**：在核心引擎（Core Engine）的過濾鏈中完成
   - **影響**：所有權限檢查、餘額驗證、業務規則都在協議層統一處理

2. **不再支持事件取消**
   - NDS 協議**不支持**通過事件監聽器來取消交易
   - 所有驗證必須在發布前完成，或通過 `NdsResult` 表達失敗

3. **遷移建議**
   ```java
   // 舊代碼：事件監聽器
   @EventHandler
   public void onTransaction(EconomyTransactionEvent event) {
       // 業務邏輯
   }
   
   // 新代碼：在發布前驗證，或處理結果
   public void processTransaction(Player player, BigDecimal amount) {
       // 方式 1：發布前驗證
       runtime.query().queryBalance(assetId, identity)
           .thenAcceptAsync(balanceResult -> {
               if (balanceResult.isSuccess() && balanceResult.data().compareTo(amount) >= 0) {
                   // 餘額足夠，發布交易
                   publishTransaction(amount);
               } else {
                   player.sendMessage("餘額不足");
               }
           }, runtime.mainThreadExecutor());
       
       // 方式 2：直接發布，處理結果
       publishTransaction(amount)
           .thenAcceptAsync(result -> {
               if (!result.isSuccess()) {
                   // 處理失敗（可能是餘額不足、權限問題等）
                   handleFailure(result.error());
               }
           }, runtime.mainThreadExecutor());
   }
   ```

---

### 11.3 命名空間與標識符（Namespace & Identifiers）

#### AssetId 格式規範

NDS 協議採用 `scope:name` 格式，類似 Minecraft 原生的 NamespaceKey 模式。

**格式說明：**
```
scope:name
```

- **scope**：作用域（`PLAYER`、`SERVER`、`GLOBAL`）
- **name**：資產名稱（小寫字母、數字、底線）

**範例：**
```java
// ✅ 正確格式
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
// fullId() = "player:coins"

AssetId bossHp = AssetId.of(AssetScope.SERVER, "world_boss_hp");
// fullId() = "server:world_boss_hp"

// 從字符串解析
AssetId fromString = AssetId.fromString("player:coins");
```

#### 遷移步驟

**步驟 1：映射舊資產名稱到新格式**
```java
// 舊代碼：直接使用字符串
String digitalName = "coins";
getPlayerDigitalAmount(playerUUID, digitalName);

// 新代碼：使用 AssetId
AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
runtime.query().queryBalance(assetId, identity);
```

**步驟 2：解決命名衝突**
舊版插件可能有多個插件使用相同的「金錢名稱」，導致衝突。新協議通過 `scope:name` 解決：

```java
// 插件 A 的貨幣
AssetId pluginACoins = AssetId.of(AssetScope.PLAYER, "plugin_a_coins");

// 插件 B 的貨幣
AssetId pluginBCoins = AssetId.of(AssetScope.PLAYER, "plugin_b_coins");

// 兩者不會衝突，因為是獨立的 AssetId
```

**步驟 3：遷移全局資產**
```java
// 舊代碼：全局數值
getServerDigitalAmount("world_boss_hp");

// 新代碼：服務器作用域資產
AssetId bossHp = AssetId.of(AssetScope.SERVER, "world_boss_hp");
NdsIdentity serverIdentity = null; // 服務器資產不需要 identity
runtime.query().queryBalance(bossHp, serverIdentity);
```

---

### 11.4 Vault 適配層的「副作用」處理

#### 風險說明

由於 NDS 底層是**全異步架構**，當舊插件在主線程調用 Vault API 時，適配層會嘗試使用 `join()` 或 `.get()` 阻塞等待。

**潛在問題：**
- 在 **Folia**（多線程伺服器）環境下可能導致線程卡頓
- 在高負載環境下可能影響主線程性能
- 阻塞時間取決於資料庫響應速度

#### 適配層實作範例

```java
// Vault 適配層（簡化示例）
public class VaultEconomyAdapter implements Economy {
    
    @Override
    public double getBalance(Player player) {
        // ⚠️ 警告：這是阻塞調用
        AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
        NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
        
        try {
            // 阻塞等待結果（不推薦，但 Vault API 要求同步）
            CompletableFuture<NdsResult<BigDecimal>> future = 
                runtime.query().queryBalance(assetId, identity);
            
            NdsResult<BigDecimal> result = future.get(5, TimeUnit.SECONDS); // 5秒超時
            
            if (result.isSuccess()) {
                return result.data().doubleValue(); // 精度損失（已文檔化）
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

#### 遷移建議

**階段 1：識別 Vault 調用**
```java
// 在舊插件中搜索
economy.getBalance(...)
economy.withdrawPlayer(...)
economy.depositPlayer(...)
```

**階段 2：逐步替換為 NDS 原生 API**
```java
// ❌ 舊代碼（Vault）
double balance = economy.getBalance(player);
economy.withdrawPlayer(player, 100.0);

// ✅ 新代碼（NDS 原生）
AssetId assetId = AssetId.of(AssetScope.PLAYER, "coins");
NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());

// 查詢餘額
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // 使用餘額
        }
    }, runtime.mainThreadExecutor());

// 扣除金額
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
            // 扣除成功
        }
    }, runtime.mainThreadExecutor());
```

**階段 3：監控與優化**
- 監控 Vault 調用頻率
- 優先遷移高頻調用的插件
- 在 Folia 環境下特別注意性能影響

---

### 11.5 數據審計與溯源（Audit Log）的接入

#### 核心概念

每一次透過協議進行的變動都可以附加 **Reason（原因）** 和 **Metadata（元數據）**，以便 Web 端管理介面能正確顯示交易來源。

#### Reason 的使用

**目的：** 標識交易的原因，用於審計和顯示。

```java
// ✅ 正確：提供明確的 reason
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("SHOP_PURCHASE")  // 明確標識為商店購買
    .build();

// ❌ 錯誤：缺少 reason 或使用模糊描述
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .reason("transaction")  // 太模糊，無法區分交易類型
    .build();
```

#### Reason 命名規範

建議使用大寫字母和底線，類似枚舉常量：

```java
// ✅ 推薦格式
.reason("SHOP_PURCHASE")
.reason("QUEST_REWARD")
.reason("ADMIN_SET")
.reason("PLAYER_TRANSFER")
.reason("AUTOMATED_REFUND")

// ❌ 不推薦
.reason("shop purchase")  // 小寫，有空格
.reason("購買")  // 非英文（雖然支持，但不便於國際化）
```

#### Metadata 的使用

**目的：** 附加額外的上下文信息，不影響業務邏輯。

```java
// 創建 metadata
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
    .metadata(metadata)  // 附加元數據
    .build();
```

#### 遷移步驟

**步驟 1：識別現有交易邏輯**
```java
// 舊代碼：可能沒有 reason
givePlayerDigital(playerUUID, "coins", amount);

// 新代碼：必須提供 reason
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(amount)
    .consistency(ConsistencyMode.STRONG)
    .reason("QUEST_REWARD")  // 明確標識
    .build();
```

**步驟 2：建立 Reason 常量類**
```java
public class TransactionReasons {
    public static final String SHOP_PURCHASE = "SHOP_PURCHASE";
    public static final String QUEST_REWARD = "QUEST_REWARD";
    public static final String ADMIN_SET = "ADMIN_SET";
    public static final String PLAYER_TRANSFER = "PLAYER_TRANSFER";
    // ...
}
```

**步驟 3：在 Web 管理介面中顯示**
Web 端可以根據 `reason` 和 `metadata` 顯示更友好的交易記錄：

```
交易記錄：
- 時間：2024-01-15 10:30:00
- 類型：商店購買 (SHOP_PURCHASE)
- 商店：diamond_shop_001
- 物品：diamond_sword
- 金額：-100 coins
```

---

### 11.6 遷移檢查清單

在開始遷移前，請確認：

- [ ] 所有數值操作已轉換為 `BigDecimal`
- [ ] 所有同步調用已改為異步回調（`.thenAccept()` 等）
- [ ] 所有 `NdsResult` 都檢查了 `isSuccess()`
- [ ] 所有 Bukkit API 調用都使用了 `mainThreadExecutor()`
- [ ] 所有資產名稱已映射到 `AssetId`（`scope:name` 格式）
- [ ] 所有交易都提供了明確的 `reason`
- [ ] 所有 Vault 調用已識別並計劃遷移
- [ ] 所有事件監聽器邏輯已轉移到結果處理

---

## 12. Final Statement

This migration is not a refactor for cleanliness.

It is a **strategic evolution**:
- From plugin → platform
- From feature → protocol
- From convenience → correctness

All future decisions must reinforce this direction.

---

**Version**: 2.0.0  
**Last Updated**: 2025-12-22  
**Status**: ✅ Stable
