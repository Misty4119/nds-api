# NDS API v2.0 開發者指南 - Next-Generation Economy Protocol

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *Vault 的繼承者，而非替代品 — 協議層。*

---

## 📘 核心定位

**NDS 不是另一個經濟插件，而是 Minecraft 生態系統中的「經濟/狀態協議層」。**

如同：
- **HTTP** 之於 Web
- **JDBC** 之於資料庫
- **MCP** 之於 AI 工具

NDS 提供統一的狀態管理協議，讓插件作者專注於業務邏輯，無需管理核心經濟狀態。

---

## ⚖️ 協議裁決與合規

本指南由 **Noie Team** 維護的官方規範。最終解釋權歸 NDS 協議所有者所有。

**合規範圍**：
- **強制執行**：NDS 不會阻止非合規插件運行（合法的 API 呼叫會被執行）
- **官方認證**：僅合規插件可獲得「NDS-native」標記與官方推薦
- **保障範圍**：非合規插件不享有未來相容性、一致性或效能保障

---

## 🎯 設計原則（岩層規範）

### 1. 協議優先（Protocol First）

**API 是協議，不是工具，不是實作。**

- API 層對 Bukkit/Paper/Database/Network **零依賴**
- 僅定義介面與契約
- 實作隔離於 nds-core 模組

### 2. 事件即真相來源（Event Is The Source Of Truth）

**狀態只能透過事件投影取得。禁止直接修改狀態。**

- 所有狀態變更必須透過事件
- 狀態由事件歷史計算得出（事件溯源）
- 可從歷史事件重建任意時間點

### 3. 可重放設計（Replayable By Design）

**可從歷史事件重建任意時間點。**

- 所有事件必須可序列化
- 事件是不可變的歷史記錄
- 投影是純函數（無副作用）

### 4. AI 就緒預設（AI-Ready Default）

**所有資料結構必須可語義化、向量化與分析。**

- 支援標籤與元資料
- 所有資產與事件可語義化
- 為未來 AI 分析而建構

### 5. 實作隔離（Implementation Isolation）

**API 層不得依賴具體實作。**

- 僅定義介面與契約
- 無 Bukkit/Paper/Database/Network 依賴
- 協議層完全隔離

---

## 🔒 不可破壞原則

以下原則在 NDS 的所有版本中**不可協商且不可變更**。這些原則是 NDS 協議的核心基礎，任何違反這些原則的變更都將破壞協議一致性。

### 原則 1：NDS 永遠是唯一真相來源

**不可變性**：此原則在任何版本中都不會改變。

- NDS 是經濟狀態的唯一真相來源
- 插件**不得**自行管理任何經濟/狀態資料
- 所有狀態查詢與修改**必須**透過 NDS API
- 狀態只能透過事件投影取得
- 違反此原則將導致狀態不一致，NDS 不保證行為正確性

### 原則 2：API 永遠非同步（Async-first）

**不可變性**：NDS 永遠不會提供同步 API。

- 所有 API 方法**必須**回傳 `CompletableFuture<NdsResult<T>>`
- NDS **不會**提供任何同步 API
- 插件**必須**使用非同步回調處理結果
- 在主執行緒阻塞 Future 是**禁止**的設計錯誤
- 在回調中呼叫 Bukkit API 時使用 `runtime.defaultExecutor()`（推薦）或 `runtime.mainThreadExecutor()`（向後兼容）

### 原則 3：核心數值永遠使用 BigDecimal

**不可變性**：NDS 永遠不會改用 double 或其他數值型別。

- 所有核心數值運算**必須**使用 `BigDecimal`
- 精度保證是 NDS 協議的核心特性
- API 方法**不會**接受 `double` 作為主要參數
- 使用 `double` 進行經濟計算是**禁止**的

### 原則 4：事件驅動架構

**不可變性**：狀態變更必須永遠透過事件。

- 所有狀態變更**必須**以事件表達
- 事件是不可變且可序列化的
- 狀態由事件歷史計算得出
- 直接修改狀態是**禁止**的

### 原則 5：結果導向錯誤處理

**不可變性**：錯誤以 `NdsResult` 表達，而非例外。

- 業務失敗以 `NdsResult.isSuccess() == false` 表達
- 系統錯誤以 `.exceptionally()` 中的例外表達
- **必須**在存取 `.data()` 前檢查 `NdsResult.isSuccess()`
- 不得使用例外判斷業務結果

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

❌ **絕對禁止：**
- ❌ 不得在 `CompletableFuture` 上使用 `.get()`（阻塞主執行緒）
- ❌ 不得使用 `double`、`float` 或 `int` 作為經濟數值
- ❌ 不得在本地儲存任何經濟/狀態資料
- ❌ 不得快取餘額或資產數值
- ❌ 不得在 `NdsPayload` 中放入 Bukkit/JVM 物件
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

在開始使用 NDS API v2.0 之前，您需要在專案中設定依賴。

### 1. Gradle (Kotlin DSL) - 推薦

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // 使用 compileOnly，因為 NDS 會在執行時提供此 API
    compileOnly("io.github.misty4119:noiedigitalsystem-api:2.0.0")
}
```

### 2. Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compileOnly 'io.github.misty4119:noiedigitalsystem-api:2.0.0'
}
```

### 3. Maven

```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.misty4119</groupId>
        <artifactId>noiedigitalsystem-api</artifactId>
        <version>2.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 🚀 快速開始

### 取得 Runtime 實例

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

// 檢查 NDS 是否已初始化
if (!NdsProvider.isInitialized()) {
    getLogger().severe("NDS 未初始化！");
    return;
}

NdsRuntime runtime = NdsProvider.get();
```

### 建立身份

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

// 輕量級建立（無需非同步查詢）
NdsIdentity player = NdsIdentity.fromString("550e8400-e29b-41d4-a716-446655440000");
// 或
NdsIdentity player = NdsIdentity.of("550e8400-e29b-41d4-a716-446655440000", IdentityType.PLAYER);
```

### 建立資產 ID

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
// 或
AssetId coins = AssetId.fromString("player:coins");
```

### 查詢餘額

```java
import java.math.BigDecimal;

runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            player.sendMessage("餘額: " + balance);
        } else {
            player.sendMessage("查詢餘額失敗: " + result.error().message());
        }
    }, runtime.defaultExecutor())
    .exceptionally(ex -> {
        getLogger().severe("錯誤: " + ex.getMessage());
        return null;
    });
```

### 建立並發布交易

```java
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

// 建立交易
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))  // 正數 = 增加，負數 = 減少
    .consistency(ConsistencyMode.STRONG)
    .source(sourceIdentity)  // 選填，用於轉帳
    .target(targetIdentity)   // 選填，用於轉帳
    .reason("purchase")       // 選填
    .build();

// 發布交易（Future 完成時表示已持久化）
runtime.eventBus().publish(transaction)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            // 交易已成功持久化
            player.sendMessage("交易完成！");
        } else {
            player.sendMessage("交易失敗: " + result.error().message());
        }
    }, runtime.defaultExecutor())
    .exceptionally(ex -> {
        getLogger().severe("錯誤: " + ex.getMessage());
        return null;
    });
```

---

## 📚 API 總覽

**錯誤處理**：`NdsResult.isSuccess() == false` = 業務失敗（預期），`Exception` = 系統錯誤（非預期）。不得使用例外判斷業務結果。

**核心服務**：
- `runtime.query()` - 透過投影查詢狀態
- `runtime.eventBus()` - 發布事件（Future 完成時表示已持久化）
- `runtime.identity()` - 身份管理

**關鍵方法**：
- `queryBalance(assetId, identity)` → `CompletableFuture<NdsResult<BigDecimal>>`
- `publish(event)` → `CompletableFuture<NdsResult<Void>>`
- `NdsTransactionBuilder.create().actor().asset().delta().consistency().build()`

---

## ❌ 常見反模式

### ❌ 反模式 1：在插件中快取餘額

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

**✅ 正確做法**：總是從 NDS 查詢

```java
// ✅ 正確：總是查詢最新狀態
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // 使用最新餘額
        }
    });
```

### ❌ 反模式 2：未檢查 NdsResult 成功狀態

```java
// ❌ 錯誤：未檢查結果
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        BigDecimal balance = result.data(); // 失敗時會拋出例外！
    });
```

**問題**：若查詢失敗，存取 `.data()` 會拋出 `IllegalStateException`。

**✅ 正確做法**：存取資料前檢查結果

```java
// ✅ 正確：檢查結果
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // 使用餘額
        } else {
            // 處理失敗
            result.onFailure(error -> {
                getLogger().severe("失敗: " + error.message());
            });
        }
    });
```

### ❌ 反模式 3：阻塞主執行緒

```java
// ❌ 錯誤：阻塞主執行緒
public void onPlayerCommand(Player player) {
    NdsResult<BigDecimal> result = runtime.query().queryBalance(assetId, identity).get(); // 阻塞！
    BigDecimal balance = result.data();
    player.sendMessage("餘額: " + balance);
}
```

**問題**：會導致伺服器延遲，違反非同步設計原則。

**✅ 正確做法**：使用回調

```java
// ✅ 正確：非同步回調
public void onPlayerCommand(Player player) {
    runtime.query().queryBalance(assetId, identity)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                player.sendMessage("餘額: " + result.data());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            player.sendMessage("查詢失敗: " + ex.getMessage());
            return null;
        });
}
```

### ❌ 反模式 4：在非同步執行緒中呼叫 Bukkit API

```java
// ❌ 錯誤：在非同步執行緒中呼叫 Bukkit API
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        player.sendMessage("餘額: " + result.data()); // 可能拋出例外！
    });
```

**問題**：Bukkit API 不是執行緒安全的。所有 Bukkit 操作必須在主執行緒執行。

**✅ 正確做法**：使用預設執行器（推薦）或主執行緒執行器（向後兼容）

```java
// ✅ 正確：使用預設執行器（推薦，v2.0.0 新增）
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("餘額: " + result.data());
        }
    }, runtime.defaultExecutor());

// ✅ 正確：使用主執行緒執行器（向後兼容，已棄用但仍可用）
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        if (result.isSuccess()) {
            player.sendMessage("餘額: " + result.data());
        }
    }, runtime.mainThreadExecutor());
```

### ❌ 反模式 5：使用基本型別而非 BigDecimal

```java
// ❌ 錯誤：精度問題
double price = 100.5;
int amount = 100;

// ✅ 正確：必須使用 BigDecimal
BigDecimal price = BigDecimal.valueOf(100.5);
BigDecimal amount = BigDecimal.valueOf(100);
// 或
BigDecimal price = new BigDecimal("100.5");
```

### ❌ 反模式 6：在 Payload 中放入 Bukkit 物件

```java
// ❌ 錯誤：在 payload 中放入 Bukkit 物件
NdsPayload payload = NdsPayload.builder()
    .put("item", itemStack)  // 非法！
    .put("location", location)  // 非法！
    .build();

// ✅ 正確：僅使用基本型別
NdsPayload payload = NdsPayload.builder()
    .put("itemId", "diamond")
    .put("world", "world")
    .put("x", location.getX())
    .put("y", location.getY())
    .put("z", location.getZ())
    .build();
```

### ❌ 反模式 7：使用 EventBuilder 建立交易

```java
// ❌ 錯誤：無法轉型
NdsEvent event = NdsEventBuilder.create()
    .actor(identity)
    .type(EventType.TRANSACTION)
    .build();
NdsTransaction transaction = (NdsTransaction) event; // 編譯錯誤！

// ✅ 正確：使用 TransactionBuilder
NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(BigDecimal.valueOf(100))
    .consistency(ConsistencyMode.STRONG)
    .build();
```

---

## 🎨 設計模式：結果導向設計

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

**問題**：步驟多、易出錯、有競態條件風險。此設計違反 NDS 協議原則。

### NDS 原生設計（必須）

```text
玩家點擊購買
↓
建立並發布交易（原子操作）
↓
成功 → 發放獎勵
失敗 → 提供回饋
```

**優點**：
- 步驟少、邏輯清晰
- NDS 保證原子性
- 無競態條件
- 自動處理並發
- 透過事件提供完整稽核軌跡

### 實際範例

```java
// ✅ NDS 原生設計：結果導向
public void onPlayerPurchase(Player player, AssetId itemAssetId, BigDecimal price) {
    NdsIdentity identity = NdsIdentity.fromString(player.getUniqueId().toString());
    AssetId coinsAssetId = AssetId.of(AssetScope.PLAYER, "coins");
    
    // 建立交易（負數 delta = 扣款）
    NdsTransaction transaction = NdsTransactionBuilder.create()
        .actor(identity)
        .asset(coinsAssetId)
        .delta(price.negate())  // 負數 = 減少
        .consistency(ConsistencyMode.STRONG)
        .reason("purchase:" + itemAssetId.name())
        .build();
    
    // 發布交易
    runtime.eventBus().publish(transaction)
        .thenAcceptAsync(result -> {
            if (result.isSuccess()) {
                // 交易成功，發放物品
                player.getInventory().addItem(itemStack);
                player.sendMessage("購買成功！");
            } else {
                // 交易失敗（例如：餘額不足）
                player.sendMessage("購買失敗: " + result.error().message());
            }
        }, runtime.defaultExecutor())
        .exceptionally(ex -> {
            // 處理例外
            getLogger().severe("購買失敗: " + ex.getMessage());
            player.sendMessage("購買失敗，請稍後再試");
            return null;
        });
}
```

---

## 📋 責任邊界

明確理解「什麼是 NDS 的責任，什麼是插件的責任」：

| 責任項目 | NDS 負責 | 插件負責 |
|---------|---------|---------|
| **狀態一致性** | ✅ 保證所有狀態一致 | ❌ 不應自行管理狀態 |
| **精度處理** | ✅ 使用 BigDecimal 保證精度 | ❌ 不應使用 double |
| **原子交易** | ✅ 保證操作原子性 | ❌ 不應自行實作交易邏輯 |
| **跨伺服器同步** | ✅ 自動同步多伺服器 | ❌ 不應自行處理同步 |
| **事件溯源** | ✅ 儲存與重放事件 | ❌ 不應管理事件儲存 |
| **業務邏輯** | ❌ 不關心業務邏輯 | ✅ 實作商店、任務等邏輯 |
| **UI / 回饋** | ❌ 不提供 UI | ✅ 提供玩家介面與訊息 |
| **資料驗證** | ✅ 驗證數值合法性 | ✅ 驗證業務規則（如價格） |

---

## ⚠️ 關鍵規則

1. **永遠不阻塞主執行緒**：使用回調，永遠不使用 `.get()`
2. **存取 `.data()` 前永遠檢查 `NdsResult.isSuccess()`**
3. **經濟數值永遠使用 `BigDecimal`**（永遠不使用 `double`）
4. **資產名稱**：僅小寫字母與底線（例如：`coins`、`world_boss_hp`）
5. **事件發布**：Future 完成時表示**已持久化**，而非僅排隊
6. **執行器使用**：在回調中呼叫 Bukkit API 時使用 `runtime.defaultExecutor()`（推薦）或 `runtime.mainThreadExecutor()`（向後兼容）

---

## 🏗️ 架構圖

```
您的插件
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← 協議層（本模組）
│   (僅介面)            │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← 實作（nds-core 模組）
│   (事件儲存,         │
│    投影)             │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(事件儲存)  (同步 / 快取)
```

**關鍵要點**：
- NDS API v2.0 是**協議層**，不是實作
- 您的插件向 NDS**請求狀態**，不**擁有狀態**
- 所有狀態變更透過**事件**（事件溯源）
- 狀態透過**投影**取得（非直接讀取）
- NDS 處理：精度、原子性、跨伺服器同步、可重放性
- **平台抽象**：透過 `NdsPlatform` 接口支援不同運行環境（Minecraft、Spring Boot、通用 Java）

---

## 📖 資源

- **AGENTS.md**：AI 開發上下文與模式
- **JavaDoc**：參見介面註解
- **遷移指南**：[nds_migration_next_gen_vault_replacement_plan_for_minecraft.md](./nds_migration_next_gen_vault_replacement_plan_for_minecraft.md)
- **通用 Java 開發者指南**：[GENERAL_JAVA_GUIDE.md](./GENERAL_JAVA_GUIDE.md)（適用於非 Minecraft 環境）

---

## 🎯 總結：成為 NDS 原生插件作者

當您遵循本指南開發插件時，您已成為**NDS 原生插件作者**：

✅ **您不再需要：**
- 管理經濟狀態
- 處理精度問題
- 擔心跨伺服器同步
- 實作原子交易
- 儲存事件歷史

✅ **您只需要：**
- 專注業務邏輯
- 使用 NDS API v2.0（`NdsProvider`、`NdsRuntime`、`NdsTransactionBuilder`）
- 處理 `NdsResult` 回調
- 提供使用者體驗
- 建立並發布事件

---

## 📜 協議聲明

> **NDS 統一了「正確性、狀態、跨伺服器一致性與未來擴展」**
> 
> **本指南不是「建議」，而是「協議規範」。**
> 
> **違反本指南的插件將不符合 NDS 原生插件標準，**
> **不享受官方推薦與未來版本相容性保證。**
> 
> **當您開始遵循「禁止事項」而非「建議事項」時，**
> **您才真正理解 NDS 作為協議層的意義。**
> 
> **當您開始使用本指南「說不」時，**
> **您才真正行使協議所有者的權力。**

---

## 🔄 從 v1.0 遷移

**關鍵變更**：
- 入口：`NdsProvider.get()` → `NdsRuntime`（非 `NoieDigitalSystem.getAPI()`）
- 錯誤處理：`NdsResult<T>`（非 `CompletableFuture<Boolean>`）
- 交易：`NdsTransactionBuilder`（非直接 API 呼叫）
- 身份：`NdsIdentity`（非 `UUID`）
- 資產：`AssetId`（非字串名稱）

**遷移**：舊 API 已棄用但可透過橋接使用。新插件**必須**使用 v2.0。

---

**版本**：2.0.0  
**專案成立**：2025-12-22  
**最後更新**：2026-01-27  
**狀態**：✅ 穩定

---

## 📝 重要說明

### Maven Central 發布狀態

- **座標**：`io.github.misty4119:noiedigitalsystem-api:2.0.0`
- **發布位置**：Maven Central（以 GitHub 命名空間作為 groupId）

### 平台支援

NDS-API 透過 `NdsPlatform` 接口支援多種運行環境：

- **Minecraft**（Spigot/Paper/Folia）：透過 `nds-api-plugin` 中的 `SpigotPlatform` 實作
- **Spring Boot**：`SpringNdsPlatform`（即將在 `nds-spring-boot-starter` 中提供）
- **通用 Java**：透過 `nds-core` 中的 `StandardJavaPlatform` 實作

### API 演進

- **v2.0.0**：新增 `defaultExecutor()` 和 `platform()` 方法
- **向後兼容**：`mainThreadExecutor()` 已棄用但仍可用於兼容性
- **遷移建議**：使用 `mainThreadExecutor()` 的現有代碼仍可運作，但新代碼應使用 `defaultExecutor()`
