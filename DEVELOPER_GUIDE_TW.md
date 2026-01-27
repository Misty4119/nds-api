# NDS API v2.0 開發者指南 - 次世代經濟協議

> **NDS – Next-Generation Economy Protocol (NGEP) v2.0**  
> *協議層，專為跨平台狀態管理而設計。*

---

## 📘 核心定位

**NDS 是一個跨平台的「經濟/狀態協議層」。**

如同：
- **HTTP** 之於 Web
- **JDBC** 之於資料庫
- **MCP** 之於 AI 工具

NDS 提供統一的狀態管理協議，讓開發者專注於業務邏輯，無需管理核心經濟狀態。

### 支援平台

| 平台 | SDK | 狀態 |
|------|-----|------|
| Java (通用) | `noiedigitalsystem-api` | ✅ 穩定 |
| .NET / C# | `Noie.Nds.Api` | ✅ 穩定 |
| Minecraft | 參見 [MINECRAFT_DEVELOPER_GUIDE_TW.md](./MINECRAFT_DEVELOPER_GUIDE_TW.md) | ✅ 穩定 |

---

## ⚖️ 協議裁決與合規

本指南由 **Noie Team** 維護的官方規範。最終解釋權歸 NDS 協議所有者所有。

**合規範圍**：
- **強制執行**：NDS 不會阻止非合規應用程式運行（合法的 API 呼叫會被執行）
- **官方認證**：僅合規應用程式可獲得「NDS-native」標記與官方推薦
- **保障範圍**：非合規應用程式不享有未來相容性、一致性或效能保障

---

## 🎯 設計原則（岩層規範）

### 1. 協議優先（Protocol First）

**API 是協議，不是工具，不是實作。**

- API 層對平台特定框架**零依賴**
- 僅定義介面與契約
- 實作隔離於核心模組

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
- 無平台特定依賴
- 協議層完全隔離

---

## 🔒 不可破壞原則

以下原則在 NDS 的所有版本中**不可協商且不可變更**。

### 原則 1：NDS 永遠是唯一真相來源

- NDS 是經濟狀態的唯一真相來源
- 應用程式**不得**自行管理任何經濟/狀態資料
- 所有狀態查詢與修改**必須**透過 NDS API
- 狀態只能透過事件投影取得

### 原則 2：API 永遠非同步（Async-first）

- **Java**：所有 API 方法回傳 `CompletableFuture<NdsResult<T>>`
- **C#**：所有 API 方法回傳 `Task<NdsResult<T>>`
- 阻塞呼叫線程是**禁止**的設計錯誤

### 原則 3：核心數值永遠使用精確型別

- **Java**：必須使用 `BigDecimal`
- **C#**：必須使用 `decimal`
- 禁止使用 `double` 或 `float` 進行經濟計算

### 原則 4：事件驅動架構

- 所有狀態變更**必須**以事件表達
- 事件是不可變且可序列化的
- 直接修改狀態是**禁止**的

### 原則 5：結果導向錯誤處理

- 業務失敗以 `NdsResult.IsSuccess == false` 表達
- 系統錯誤以例外表達
- **必須**在存取資料前檢查結果狀態

---

## 📦 依賴設定

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

## 🚀 快速開始

### Java SDK

#### 取得 Runtime 實例

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

if (!NdsProvider.isInitialized()) {
    throw new IllegalStateException("NDS 未初始化");
}

NdsRuntime runtime = NdsProvider.get();
```

#### 建立身份與資產

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

// 建立身份
NdsIdentity user = NdsIdentity.of("user-123", IdentityType.PLAYER);

// 建立資產 ID
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

#### 查詢餘額

```java
import java.math.BigDecimal;

runtime.query().queryBalance(coins, user)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            System.out.println("餘額: " + balance);
        } else {
            System.err.println("查詢失敗: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        System.err.println("系統錯誤: " + ex.getMessage());
        return null;
    });
```

#### 建立並發布交易

```java
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

NdsTransaction transaction = NdsTransactionBuilder.create()
    .actor(user)
    .asset(coins)
    .delta(BigDecimal.valueOf(100))  // 正數 = 增加，負數 = 減少
    .consistency(ConsistencyMode.STRONG)
    .reason("deposit")
    .build();

runtime.eventBus().publish(transaction)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            System.out.println("交易完成");
        } else {
            System.err.println("交易失敗: " + result.error().message());
        }
    })
    .exceptionally(ex -> {
        System.err.println("系統錯誤: " + ex.getMessage());
        return null;
    });
```

---

### C# SDK

#### 取得 Runtime 實例

```csharp
using Noie.Nds.Api;
using Noie.Nds.Api.Identity;
using Noie.Nds.Api.Asset;

// 從依賴注入取得 runtime
INdsRuntime runtime = serviceProvider.GetRequiredService<INdsRuntime>();
```

#### 建立身份與資產

```csharp
// 建立身份
INdsIdentity user = NdsIdentity.Of("user-123", IdentityType.Player);

// 建立資產 ID
IAssetId coins = AssetId.Of(AssetScope.Player, "coins");
```

#### 查詢餘額

```csharp
var result = await runtime.Query.QueryBalanceAsync(coins, user);

if (result.IsSuccess)
{
    decimal balance = result.Data;
    Console.WriteLine($"餘額: {balance}");
}
else
{
    Console.WriteLine($"查詢失敗: {result.Error.Message}");
}
```

#### 建立並發布交易

```csharp
using Noie.Nds.Api.Transaction;

var transaction = NdsTransactionBuilder.Create()
    .Actor(user)
    .Asset(coins)
    .Delta(100m)  // 正數 = 增加，負數 = 減少
    .Consistency(ConsistencyMode.Strong)
    .Reason("deposit")
    .Build();

var result = await runtime.EventBus.PublishAsync(transaction);

if (result.IsSuccess)
{
    Console.WriteLine("交易完成");
}
else
{
    Console.WriteLine($"交易失敗: {result.Error.Message}");
}
```

---

## 📚 API 總覽

### 核心服務

| 服務 | Java | C# | 說明 |
|------|------|-----|------|
| 查詢服務 | `runtime.query()` | `runtime.Query` | 透過投影查詢狀態 |
| 事件總線 | `runtime.eventBus()` | `runtime.EventBus` | 發布事件 |
| 身份服務 | `runtime.identity()` | `runtime.Identity` | 身份管理 |

### 關鍵方法

#### Java

```java
// 查詢餘額
CompletableFuture<NdsResult<BigDecimal>> queryBalance(AssetId asset, NdsIdentity identity);

// 發布事件
CompletableFuture<NdsResult<Void>> publish(NdsEvent event);

// 建立交易
NdsTransactionBuilder.create()
    .actor(identity)
    .asset(assetId)
    .delta(amount)
    .consistency(mode)
    .build();
```

#### C#

```csharp
// 查詢餘額
Task<NdsResult<decimal>> QueryBalanceAsync(IAssetId asset, INdsIdentity identity);

// 發布事件
Task<NdsResult<Unit>> PublishAsync(INdsEvent @event);

// 建立交易
NdsTransactionBuilder.Create()
    .Actor(identity)
    .Asset(assetId)
    .Delta(amount)
    .Consistency(mode)
    .Build();
```

---

## ❌ 常見反模式

### 反模式 1：在應用程式中快取餘額

```java
// ❌ 錯誤：快取會導致狀態不同步
private final Map<String, BigDecimal> balanceCache = new HashMap<>();
```

**問題**：其他服務或節點可能已修改餘額，快取會導致狀態不一致。

**✅ 正確做法**：總是從 NDS 查詢。

### 反模式 2：未檢查結果狀態

```java
// ❌ 錯誤：未檢查結果
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        BigDecimal balance = result.data(); // 失敗時會拋出例外！
    });
```

**✅ 正確做法**：存取資料前檢查結果。

```java
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
        } else {
            // 處理失敗
        }
    });
```

### 反模式 3：阻塞呼叫線程

```java
// ❌ 錯誤：阻塞線程
NdsResult<BigDecimal> result = runtime.query().queryBalance(assetId, identity).get();
```

```csharp
// ❌ 錯誤：阻塞線程
var result = runtime.Query.QueryBalanceAsync(assetId, identity).Result;
```

**✅ 正確做法**：使用非同步回調或 await。

### 反模式 4：使用浮點數

```java
// ❌ 錯誤：精度問題
double price = 100.5;
```

```csharp
// ❌ 錯誤：精度問題
double price = 100.5;
```

**✅ 正確做法**：

```java
// Java
BigDecimal price = new BigDecimal("100.5");
```

```csharp
// C#
decimal price = 100.5m;
```

---

## 🎨 設計模式：結果導向設計

### 傳統設計（禁止）

```text
使用者請求操作
↓
先檢查餘額
↓
再扣款
↓
再執行操作
```

**問題**：步驟多、易出錯、有競態條件風險。

### NDS 原生設計（必須）

```text
使用者請求操作
↓
建立並發布交易（原子操作）
↓
成功 → 執行後續操作
失敗 → 提供回饋
```

**優點**：
- 步驟少、邏輯清晰
- NDS 保證原子性
- 無競態條件
- 自動處理並發
- 透過事件提供完整稽核軌跡

---

## 📋 責任邊界

| 責任項目 | NDS 負責 | 應用程式負責 |
|---------|---------|-------------|
| **狀態一致性** | ✅ 保證所有狀態一致 | ❌ 不應自行管理狀態 |
| **精度處理** | ✅ 使用精確型別保證精度 | ❌ 不應使用浮點數 |
| **原子交易** | ✅ 保證操作原子性 | ❌ 不應自行實作交易邏輯 |
| **跨節點同步** | ✅ 自動同步多節點 | ❌ 不應自行處理同步 |
| **事件溯源** | ✅ 儲存與重放事件 | ❌ 不應管理事件儲存 |
| **業務邏輯** | ❌ 不關心業務邏輯 | ✅ 實作應用程式邏輯 |
| **使用者介面** | ❌ 不提供 UI | ✅ 提供使用者介面 |

---

## 📋 命名規則

### 資產名稱

- **必須**使用小寫字母與底線
- **範例**：`coins`、`gold`、`stamina`、`world_boss_hp`
- **禁止**：`Coins`、`gold-coin`、`gold.coin`、`GOLD`

### 身份 ID

- **使用者**：UUID 格式（例如：`550e8400-e29b-41d4-a716-446655440000`）
- **系統**：`system:name` 格式（例如：`system:admin`）
- **AI**：`ai:name` 格式（例如：`ai:gpt-4`）
- **外部**：`external:service:name` 格式（例如：`external:payment:stripe`）

---

## 🏗️ 架構圖

```
您的應用程式
    │
    ▼
┌──────────────────────┐
│   NDS API v2.0       │  ← 協議層（本模組）
│   (僅介面)            │
└──────────────────────┘
    │
    ▼
┌──────────────────────┐
│   NDS Core          │  ← 實作
│   (事件儲存,         │
│    投影)             │
└──────────────────────┘
    │
    ▼
PostgreSQL + Redis
(事件儲存)  (同步 / 快取)
```

---

## 📖 其他資源

- **Minecraft 開發指南**：[MINECRAFT_DEVELOPER_GUIDE_TW.md](./MINECRAFT_DEVELOPER_GUIDE_TW.md)
- **英文版開發指南**：[DEVELOPER_GUIDE.md](./DEVELOPER_GUIDE.md)
- **AI 開發上下文**：[AGENTS.md](./AGENTS.md)

---

## 📜 協議聲明

> **NDS 統一了「正確性、狀態、跨節點一致性與未來擴展」**
> 
> **本指南不是「建議」，而是「協議規範」。**
> 
> **違反本指南的應用程式將不符合 NDS 原生標準，**
> **不享受官方推薦與未來版本相容性保證。**

---

**版本**：2.1.0  
**專案成立**：2025-12-22  
**最後更新**：2026-01-27  
**狀態**：✅ 穩定
