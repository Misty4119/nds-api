# 開發者指南

本指南說明如何在 Java 或 .NET 專案中整合 NDS-API。

## 安裝

### Java

請使用 Maven Central 上顯示的最新已發佈版本。

```kotlin
repositories { mavenCentral() }
dependencies { implementation("io.github.misty4119:noiedigitalsystem-api:<VERSION>") }
```

### .NET（C#）

請使用 NuGet 上顯示的最新已發佈版本。

```bash
dotnet add package Noie.Nds.Api
```

若你只需要介面與核心型別：

```bash
dotnet add package Noie.Nds.Api.Abstractions
```

## 核心概念

### Identity（身份）

Identity 表示操作主體（玩家/系統/AI/外部服務）。

**Java**

```java
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;

NdsIdentity actor = NdsIdentity.of("system:shop", IdentityType.SYSTEM);
```

**C#**

```csharp
using Noie.Nds.Api.Identity;

var actor = NdsIdentity.Of("system:shop", IdentityType.System);
```

### AssetId（資產識別）

AssetId 用來描述「要被變更的是哪一種資產」，不依賴資料庫或平台。

**Java**

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;

AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

**C#**

```csharp
using Noie.Nds.Api.Asset;

var coins = AssetId.Player("coins");
```

### 精確數值

- Java：使用 `BigDecimal`
- C#：使用 `decimal`

若你直接使用 v3 fixed-point 協議金額（units+nanos），請做精確轉換。

**C# 範例**

```csharp
using Nds.Ledger.V1;
using Noie.Nds.Api.Adapter;

Money money = MoneyAdapter.ToProto("NDS", 100.5m);
decimal amount = MoneyAdapter.FromProto(money);
```

## 結果處理

Runtime 實作通常回傳結構化結果。請務必檢查成功與否，並明確處理失敗。

**C#**

```csharp
using Noie.Nds.Api.Result;

var ok = NdsResult<int>.Success(1);
var fail = NdsResult<int>.Failure("VALIDATION_INVALID_ARGUMENT", "bad input");
```

## 協議文件

- 協議文件入口：`spec/docs/README.md`

---

Last updated: 2026-01-31

