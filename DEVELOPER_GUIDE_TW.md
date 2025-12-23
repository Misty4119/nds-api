# NDS 開發者指南 - Next-Generation Economy Protocol

> **NDS – Next-Generation Economy Protocol (NGEP)**
> *The successor of Vault, not a replacement — a protocol.*

---

## 📘 核心定位

**NDS 不是另一個經濟插件，而是 Minecraft 生態中的「經濟/狀態協議層（Protocol Layer）」。**

就像：
- **HTTP** 對 Web
- **JDBC** 對資料庫
- **MCP** 對 AI 工具

NDS 提供統一的狀態管理協議，讓插件作者專注於業務邏輯，而不需要管理核心經濟狀態。

---

## ⚖️ 協議裁決與合規（Protocol Authority & Compliance）

### 維護者聲明

本開發指南由 **NDS Core Team** 維護，是 NDS 協議的官方規範文檔。

### 最終解釋權

是否符合「NDS 原生插件」標準，**最終解釋權歸 NDS 協議所有者所有**。

本協議要求所有使用 NDS 的插件開發者遵循本指南的規範。違反本指南的插件將被視為不符合 NDS 原生插件標準。

### 合規判定

NDS 原生插件的判定標準基於本指南中明確列出的「必須做到」和「絕對禁止」條款。任何違反這些條款的行為都將導致插件不符合 NDS-native 標準。

### 協議合規的實際影響範圍

為了避免誤解，我們明確劃分「規範」、「認證」與「強制」的邊界：

1. **技術強制（Enforced）**：NDS 核心 **不會** 主動阻止非合規插件的運行。只要 API 調用合法，NDS 都會執行。
2. **官方認證（Certified）**：「NDS-native」是官方認證標準。只有符合標準的插件才能獲得官方推薦和「NDS-native」標記。
3. **保障範圍（Guaranteed）**：不合規插件雖然可以使用 NDS，但不享有未來版本兼容性、狀態一致性承諾和性能優化保障。

---

## 🎯 設計原則

### 1. 單一真相來源（Single Source of Truth）

所有經濟數值、跨插件狀態、交易結果**只能存在於 NDS**。

👉 **插件永遠「請求狀態」，而不是「擁有狀態」**

### 2. 非同步優先（Async-first by Design）

- 所有 API 回傳 `CompletableFuture`
- **禁止 blocking**（不要在主線程使用 `.get()`）
- 為高併發、Folia、跨服而生

### 3. 協議優先，功能為輔

NDS 不關心：
- 商店怎麼賣
- 任務怎麼給獎勵
- 拍賣怎麼競標

NDS 只關心：
> **狀態是否合法、是否一致、是否安全**

### 4. 安全與一致性

- **BigDecimal 精度**：避免 double 精度問題
- **交易原子性**：操作自動回滾保證
- **跨服同步**：Redis 確保多伺服器一致性

### 5. 為什麼沒有同步事件？（Architectural Decision）

NDS **刻意不提供** 同步事件掛鉤（Synchronous Event Hooks，如 `PlayerPreWithdrawEvent`），原因如下：

1. **防止 Race Conditions**：同步事件允許插件攔截並修改結果，這在異步/跨服環境下是數據不一致的主要來源。
2. **強迫結果導向**：開發者應關注「操作結果（Future）」而非「操作過程（Event）」。

這是 NDS 的架構選擇，而非功能缺失。

---

## 🔒 不可破壞原則（Non-Negotiable Principles）

以下原則在 NDS 的所有版本中**不可改變、不可協商**。這些原則是 NDS 協議的核心基礎，任何違反這些原則的變更都將破壞協議的一致性。

### 原則 1：NDS 永遠是唯一狀態來源（Single Source of Truth）

**不可改變性**：任何版本都不會改變此原則。

- NDS 是經濟狀態的唯一真相來源
- 插件**不得**自行管理任何經濟/狀態資料
- 所有狀態查詢和修改**必須**通過 NDS API
- 違反此原則將導致狀態不一致，NDS 不保證行為正確性

### 原則 2：API 永遠非同步（Async-first）

**不可改變性**：NDS 永遠不會提供同步 API。

- 所有 API 方法**必須**返回 `CompletableFuture`
- NDS **不會**提供任何同步 API（如 `.get()` 的便捷方法）
- 插件**必須**使用非同步回調處理結果
- 在主線程阻塞 Future 是**禁止**的設計錯誤

### 原則 3：核心數值永遠使用 BigDecimal

**不可改變性**：NDS 永遠不會改用 double 或其他數值類型。

- 所有核心數值操作**必須**使用 `BigDecimal`
- 精度保證是 NDS 協議的核心特性
- API 方法**不會**接受 `double` 作為主要參數（僅提供向後兼容的默認方法）
- 使用 `double` 進行經濟計算是**禁止**的

### 原則 4：Vault 永遠只是相容層

**不可改變性**：Vault Bridge 永遠不會成為 NDS 的核心功能。

- Vault Bridge 僅用於**舊插件兼容**
- 新開發的插件**必須**使用 NDS API，不得使用 Vault API
- 使用 Vault API 的插件將被視為 Legacy Plugin
- NDS 的核心演進將圍繞 NDS API，而非 Vault API

### 版本兼容性保證

這些原則在 NDS 的所有未來版本中都將保持不變。任何違反這些原則的變更都將被視為破壞性變更，並會明確標記為不兼容版本。

---

## 🔥 什麼是 NDS 原生插件（NDS-native Plugin）？

### NDS 原生插件定義

**NDS 原生插件 = 必須同時滿足以下條件：**

✅ **必須做到：**
- ✅ 所有狀態來自 NDS（不存任何經濟/狀態資料）
- ✅ 所有行為以「結果回調」驅動（非同步優先）
- ✅ 使用 NDS API 直接操作（不使用 Vault API）
- ✅ 正確處理異常和失敗情況

❌ **絕對禁止：**
- ❌ 不存任何經濟/狀態資料到本地
- ❌ 不 cache Digital 數值（狀態由 NDS 管理）
- ❌ 不假設狀態一定存在（總是檢查結果）
- ❌ 不使用 Vault API（新插件必須直接使用 NDS API）
- ❌ 不在主線程 blocking Future（使用 `.get()`）

### 為什麼要成為 NDS 原生插件？

1. **狀態一致性保證**：所有狀態由 NDS 統一管理，不會出現不同步
2. **跨服兼容**：自動支援多伺服器環境
3. **未來擴展性**：可以無縫使用 NDS 的新功能
4. **性能優化**：NDS 內部優化會自動應用到你的插件

---

## ⚠️ 違規後果（Enforcement & Consequences）

本節明確說明違反 NDS 協議規範的後果。這些後果不是威脅，而是協議自我保護的必要措施。

### 不符合 NDS-native 標準的後果

如果插件不符合「NDS 原生插件」標準（違反「必須做到」或「絕對禁止」條款），將導致：

1. **不列入官方推薦列表**
   - 不會出現在 NDS 官方推薦的插件列表中
   - 不會獲得「NDS 原生插件」認證標記

2. **不保證與 NDS 未來版本的兼容性**
   - NDS 的未來演進可能導致這些插件無法正常工作
   - 需要自行承擔升級和維護成本

3. **不享受 NDS 原生插件的優勢**
   - 無法使用 NDS 的新功能
   - 無法獲得 NDS 原生插件的性能優化
   - 跨服環境下可能出現狀態不一致問題

### 違反核心原則的後果

違反「不可破壞原則」將導致：

1. **不保證行為正確性**
   - NDS 無法保證插件的狀態一致性
   - 可能出現數據丟失、重複扣款等嚴重問題

2. **可能導致狀態不一致**
   - 跨插件狀態可能不同步
   - 跨服環境下狀態可能出現衝突

3. **跨服環境下可能出現問題**
   - Redis 同步可能失效
   - 多伺服器環境下狀態可能不一致

### Legacy Plugin 的定位

使用 Vault API 的新插件將被視為 **Legacy Plugin**：

- **僅視為 Legacy Plugin**：不會被視為 NDS 原生插件
- **不享受 NDS 原生插件的優勢**：無法使用 NDS 的新功能和優化
- **技術債務**：需要未來遷移到 NDS API
- **維護成本**：需要自行處理 Vault API 的限制（精度、同步等）

### 官方推薦政策

NDS 官方只推薦符合以下條件的插件：

- ✅ 完全符合「NDS 原生插件」標準
- ✅ 遵循所有「不可破壞原則」
- ✅ 通過 NDS 原生插件檢查清單
- ✅ 使用 NDS API 而非 Vault API

不符合上述條件的插件不會獲得官方推薦，但仍可正常使用 NDS（通過 Vault Bridge 或部分使用 NDS API）。

---

## ❌ 常見錯誤設計（Anti-Patterns）

### ❌ 錯誤 1：在插件中快取餘額

```java
// ❌ 錯誤：快取會導致狀態不同步
private final Map<UUID, BigDecimal> balanceCache = new HashMap<>();

public void checkBalance(UUID uuid) {
    if (balanceCache.containsKey(uuid)) {
        return balanceCache.get(uuid); // 錯誤：可能已過時
    }
    // ...
}
```

**問題**：其他插件或伺服器可能已修改餘額，快取會導致狀態不一致。

**✅ 正確做法**：每次都從 NDS 查詢

```java
// ✅ 正確：每次都查詢最新狀態
public void checkBalance(UUID uuid) {
    api.getPlayerDigitalAmount(uuid, "coins")
        .thenAccept(balance -> {
            // 使用最新餘額
        });
}
```

### ❌ 錯誤 2：假設 take 一定成功

```java
// ❌ 錯誤：沒有檢查結果
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100));
player.getInventory().addItem(new ItemStack(Material.DIAMOND)); // 可能白給物品
```

**問題**：如果餘額不足，物品已經給了，但錢沒扣。

**✅ 正確做法**：檢查結果再執行

```java
// ✅ 正確：檢查結果
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            // 扣款成功，才給物品
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        } else {
            player.sendMessage("餘額不足！");
        }
    });
```

### ❌ 錯誤 3：在主線程阻塞

```java
// ❌ 錯誤：阻塞主線程
public void onPlayerCommand(Player player) {
    BigDecimal balance = api.getPlayerDigitalAmount(player.getUniqueId(), "coins").get(); // 阻塞！
    player.sendMessage("餘額: " + balance);
}
```

**問題**：會導致伺服器卡頓，違反非同步設計原則。

**✅ 正確做法**：使用回調

```java
// ✅ 正確：非同步回調
public void onPlayerCommand(Player player) {
    api.getPlayerDigitalAmount(player.getUniqueId(), "coins")
        .thenAccept(balance -> {
            player.sendMessage("餘額: " + balance);
        })
        .exceptionally(ex -> {
            player.sendMessage("查詢失敗: " + ex.getMessage());
            return null;
        });
}
```

### ❌ 錯誤 4：先檢查再扣款（Race Condition）

```java
// ❌ 錯誤：檢查和扣款之間可能被其他操作修改
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        if (balance.compareTo(BigDecimal.valueOf(100)) >= 0) {
            api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100)); // 可能失敗
            giveItem();
        }
    });
```

**問題**：檢查和扣款之間，餘額可能被其他操作修改。

**✅ 正確做法**：直接扣款，讓 NDS 處理原子性

```java
// ✅ 正確：直接扣款，NDS 保證原子性
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            giveItem(); // 只有扣款成功才給物品
        } else {
            player.sendMessage("餘額不足！");
        }
    });
```

### ❌ 錯誤 5：不處理異常

```java
// ❌ 錯誤：沒有異常處理
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // 如果出錯，這裡不會執行，但也不會有錯誤提示
    });
```

**問題**：資料庫錯誤、網路問題等異常會被靜默忽略。

**✅ 正確做法**：總是處理異常

```java
// ✅ 正確：處理異常
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // 成功處理
    })
    .exceptionally(ex -> {
        getLogger().severe("查詢餘額失敗: " + ex.getMessage());
        player.sendMessage("查詢失敗，請稍後再試");
        return null;
    });
```

### ❌ 錯誤 6：使用 Vault API（新插件）

```java
// ❌ 錯誤：新插件不應使用 Vault API
Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
economy.withdrawPlayer(player, 100);
```

**問題**：Vault API 是同步的，精度是 double，不支援跨服。

**✅ 正確做法**：直接使用 NDS API

```java
// ✅ 正確：直接使用 NDS API
api.takePlayerDigital(player.getUniqueId(), "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        // 處理結果
    });
```

---

## 🎨 設計模式：結果導向設計（Result-driven Design）

### 傳統設計（禁止）

```text
玩家點擊購買
↓
先檢查餘額
↓
再扣款
↓
再給物品
```

**問題**：步驟多，容易出錯，有 Race Condition 風險。此設計違反 NDS 協議原則。

### NDS 原生設計（必須）

```text
玩家點擊購買
↓
請求 NDS 扣款（原子操作）
↓
成功 → 發放獎勵
失敗 → 回饋原因
```

**優點**：
- 步驟少，邏輯清晰
- NDS 保證原子性
- 無 Race Condition
- 自動處理併發

### 實際範例

```java
// ✅ NDS 原生設計：結果導向
public void onPlayerPurchase(Player player, ItemStack item, BigDecimal price) {
    UUID uuid = player.getUniqueId();
    
    // 直接扣款，讓 NDS 處理所有檢查
    api.takePlayerDigital(uuid, "coins", price)
        .thenAccept(success -> {
            if (success) {
                // 扣款成功，發放物品
                player.getInventory().addItem(item);
                player.sendMessage("購買成功！");
            } else {
                // 扣款失敗，告知原因
                api.getPlayerDigitalAmount(uuid, "coins")
                    .thenAccept(balance -> {
                        player.sendMessage("餘額不足！當前餘額: " + balance);
                    });
            }
        })
        .exceptionally(ex -> {
            // 處理異常
            getLogger().severe("購買失敗: " + ex.getMessage());
            player.sendMessage("購買失敗，請稍後再試");
            return null;
        });
}
```

---

## 📋 責任邊界（Responsibility Boundaries）

明確知道「什麼是 NDS 的責任，什麼是插件的責任」：

| 責任項目 | NDS 負責 | 插件負責 |
|---------|---------|---------|
| **狀態一致性** | ✅ 保證所有狀態一致 | ❌ 不應自行管理狀態 |
| **精度處理** | ✅ 使用 BigDecimal 保證精度 | ❌ 不應使用 double |
| **原子交易** | ✅ 保證操作原子性 | ❌ 不應自行實現交易邏輯 |
| **跨服同步** | ✅ 自動同步多伺服器 | ❌ 不應自行處理同步 |
| **業務邏輯** | ❌ 不關心業務邏輯 | ✅ 實現商店、任務等邏輯 |
| **UI / 回饋** | ❌ 不提供 UI | ✅ 提供玩家界面和訊息 |
| **數據驗證** | ✅ 驗證數值合法性 | ✅ 驗證業務規則（如價格） |

### 範例：商店插件

```java
// ✅ 正確的責任劃分
public class ShopPlugin {
    
    // 插件責任：業務邏輯和 UI
    public void onPlayerClickShopItem(Player player, ShopItem item) {
        UUID uuid = player.getUniqueId();
        BigDecimal price = item.getPrice();
        
        // NDS 責任：狀態管理和原子操作
        api.takePlayerDigital(uuid, "coins", price)
            .thenAccept(success -> {
                if (success) {
                    // 插件責任：發放物品和回饋
                    player.getInventory().addItem(item.getItemStack());
                    player.sendMessage("購買成功！");
                } else {
                    // 插件責任：告知失敗原因
                    player.sendMessage("餘額不足！");
                }
            })
            .exceptionally(ex -> {
                // 插件責任：錯誤處理和回饋
                getLogger().severe("購買失敗: " + ex.getMessage());
                player.sendMessage("購買失敗，請稍後再試");
                return null;
            });
    }
}
```

---

## 🚀 快速開始

### 取得 API 實例

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

### 基本操作範例

#### 取得餘額

```java
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        getLogger().info("玩家餘額: " + balance);
    })
    .exceptionally(ex -> {
        getLogger().severe("取得餘額失敗: " + ex.getMessage());
        return null;
    });
```

#### 扣款（一次性原子操作）

```java
api.takePlayerDigital(uuid, "coins", BigDecimal.valueOf(100))
    .thenAccept(success -> {
        if (success) {
            // 扣款成功，給予物品
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        } else {
            // 餘額不足
            player.sendMessage("餘額不足！");
        }
    })
    .exceptionally(ex -> {
        getLogger().severe("扣款失敗: " + ex.getMessage());
        return false;
    });
```

#### 增加餘額

```java
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(50))
    .thenRun(() -> {
        player.sendMessage("獲得 50 金幣！");
    })
    .exceptionally(ex -> {
        getLogger().severe("增加餘額失敗: " + ex.getMessage());
        return null;
    });
```

---

## 📚 API 方法總覽

### API 契約語意（API Contract Definitions）

在使用 `takePlayerDigital` 等回傳 `CompletableFuture<Boolean>` 的方法時，請嚴格遵循以下語意：

* **`Boolean = false`**：**業務失敗**（例如：餘額不足、扣款條件未滿足）。這是預期內的業務邏輯結果。
* **`Exception`**：**系統錯誤**（例如：資料庫斷線、Redis 同步失敗、並發寫入衝突）。這是預期外的系統故障。

> **重要**：插件 **不得** 依賴 Exception 來判斷業務結果（例如不應把「資料庫超時」當作「餘額不足」處理）。

### Player Digital Operations（最常用）

| 方法 | 說明 | 返回值 |
|------|------|--------|
| `getPlayerDigitalAmount(uuid, name)` | 取得餘額 | `CompletableFuture<BigDecimal>` |
| `givePlayerDigital(uuid, name, amount)` | 增加餘額 | `CompletableFuture<Void>` |
| `takePlayerDigital(uuid, name, amount)` | 扣款（原子操作） | `CompletableFuture<Boolean>` |
| `setPlayerDigitalAmount(uuid, name, amount)` | 設定餘額 | `CompletableFuture<Void>` |
| `getAllPlayerDigitals(uuid)` | 取得所有 Digital | `CompletableFuture<Map<String, PlayerDigital>>` |

### Server Digital Operations（全服變數）

| 方法 | 說明 | 返回值 |
|------|------|--------|
| `getServerDigitalAmount(name)` | 取得全服變數 | `CompletableFuture<BigDecimal>` |
| `giveServerDigital(name, amount)` | 增加全服變數 | `CompletableFuture<Void>` |
| `takeServerDigital(name, amount)` | 減少全服變數 | `CompletableFuture<Boolean>` |
| `setServerDigitalAmount(name, amount)` | 設定全服變數 | `CompletableFuture<Void>` |

### Global Player Digital（系統設定）

| 方法 | 說明 | 返回值 |
|------|------|--------|
| `createGlobalPlayerDigital(name, initial, limit)` | 創建全域 Digital | `CompletableFuture<Void>` |
| `isGlobalPlayerDigital(name)` | 檢查是否為全域 Digital | `CompletableFuture<Boolean>` |
| `getAllGlobalPlayerDigitals()` | 取得所有全域 Digital | `CompletableFuture<Map<String, Digital>>` |

---

## ⚠️ 重要注意事項

### 1. 禁止阻塞主線程

```java
// ❌ 錯誤：在主線程阻塞
BigDecimal balance = api.getPlayerDigitalAmount(uuid, "coins").get();

// ✅ 正確：使用 CompletableFuture 回調
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // 處理餘額
    });
```

### 2. 必須處理異常

**要求**：所有 API 調用都必須處理異常，這是 NDS 原生插件的強制要求。

```java
api.getPlayerDigitalAmount(uuid, "coins")
    .thenAccept(balance -> {
        // 成功處理
    })
    .exceptionally(ex -> {
        // 必須處理異常（強制要求）
        getLogger().severe("錯誤: " + ex.getMessage());
        return null;
    });
```

**違規後果**：不處理異常的插件不符合 NDS-native 標準，可能導致錯誤被靜默忽略。

### 3. 必須使用 BigDecimal，禁止使用 double

**要求**：所有經濟數值操作必須使用 BigDecimal，這是「不可破壞原則」之一。

```java
// ❌ 錯誤：精度問題（違反不可破壞原則）
api.givePlayerDigital(uuid, "coins", 100.0);

// ✅ 正確：必須使用 BigDecimal
api.givePlayerDigital(uuid, "coins", BigDecimal.valueOf(100));
```

**違規後果**：使用 double 進行經濟計算違反不可破壞原則，NDS 不保證行為正確性。

### 4. Digital 名稱規範

**要求**：Digital 名稱必須遵循以下規範：

- **必須**使用小寫字母和下劃線
- 範例：`coins`, `gold`, `stamina`, `world_boss_hp`
- **禁止**：`Coins`, `gold-coin`, `gold.coin`

**違規後果**：不符合命名規範的 Digital 可能無法正常工作。

---

## 🔌 Vault 兼容（Legacy Support）

> **重要**：Vault Bridge 僅用於**舊插件兼容**。新開發的插件**必須**直接使用 NDS API，不得使用 Vault API。

NDS 提供**官方 Vault Bridge**，讓舊插件無痛接入：

```
Old Plugin ── Vault ──┐
                      ▼
                 NDS Core
                      ▲
New Plugin ── NDS API ─┘
```

舊插件「以為自己在用 Vault」，實際狀態由 NDS 管理。

### 配置 Vault Bridge

在 `config.yml` 中：

```yaml
vault:
  enabled_digitals:
    - "coins"
    - "gems"
  default_currency: "coins"
```

---

## 📖 Phase 導向開發指南

### Phase 1：Legacy Plugin（使用 Vault）

**適用對象**：現有 Vault 插件，需要無痛遷移

- 繼續使用 Vault API
- NDS 作為後端透明提供狀態管理
- 無需修改代碼

### Phase 2：NDS-native Plugin（現在必須）

**適用對象**：新開發的插件

**要求**（非建議）：
- ✅ **必須**直接使用 NDS API
- ✅ **必須**遵循本指南的所有原則
- ✅ **必須**成為 NDS 原生插件

**檢查清單**（所有項目必須通過）：
- [ ] **必須**不使用 Vault API
- [ ] **必須**不 cache 任何狀態
- [ ] **必須**所有操作都是非同步
- [ ] **必須**正確處理異常
- [ ] **必須**使用結果導向設計

**注意**：不符合此檢查清單的插件將被視為不符合 NDS-native 標準，不享受官方推薦和未來版本兼容性保證。

### Phase 3：Advanced Features（未來）

**適用對象**：需要進階功能的插件

- 多 Digital 協同操作
- 跨系統狀態管理
- 自定義 Digital 類型

---

## 🏗️ 架構圖

```
Plugins (Shop / Quest / RPG / Gacha)
        │
        ▼
┌──────────────────────┐
│   NDS API / Protocol │  ← 協議層（你使用這裡）
└──────────────────────┘
        │
 ┌──────┴────────┐
 │               │
 ▼               ▼
PostgreSQL      Redis
(State)         (Sync / Cache)
```

---

## 📖 更多資源

- **API 文檔**：查看 `NoieDigitalSystemAPI` 接口註釋
- **範例插件**：參考 NDS 官方範例
- **問題回報**：GitHub Issues

---

## 💡 核心成功標準

1. **安裝 NDS 後，即便完全不用 Web/Shop/Auction，核心經濟仍可運作**
2. **插件作者易上手，無需管理核心狀態**
3. **管理員可自由選擇啟用應用層功能**
4. **跨服/高併發下核心 API 穩定可靠**
5. **官方 Vault Bridge 保證舊插件兼容**

---

## 🎯 總結：成為 NDS 原生插件作者

當你遵循本指南開發插件時，你已經成為 **NDS 原生插件作者**：

✅ **你不再需要：**
- 管理經濟狀態
- 處理精度問題
- 擔心跨服同步
- 實現原子交易

✅ **你只需要：**
- 專注業務邏輯
- 使用 NDS API
- 處理結果回調
- 提供用戶體驗

---

## 📜 協議聲明

> **NDS 統一了「正確性、狀態、跨服一致性、未來擴展」**
> 
> **本指南不是「建議」，而是「協議規範」。**
> 
> **違反本指南的插件將不符合 NDS 原生插件標準，**
> **不享受官方推薦和未來版本兼容性保證。**
> 
> **當你開始遵循「禁止事項」而不是「建議事項」時，**
> **你就真正理解了 NDS 作為協議層的意義。**
> 
> **當你開始用本指南「說不」時，**
> **你就真正行使了協議所有者的權力。**
