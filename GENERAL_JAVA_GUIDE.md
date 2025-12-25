# NDS-API 通用 Java 開發者指南

> **面向通用 Java 開發者的完整指南**

---

## 📘 為什麼選擇 NDS-API？

### 問題場景

你正在開發一個需要管理資產、餘額或狀態的 Java 應用，但面臨以下挑戰：

- **精度問題**：使用 `double` 導致浮點誤差累積
- **並發問題**：多線程環境下的狀態一致性難以保證
- **審計需求**：需要完整的操作歷史記錄
- **擴展性**：未來可能需要支持分佈式部署

### NDS-API 的解決方案

NDS-API 提供了一個輕量級、嵌入式的解決方案：

| 問題 | NDS-API 解決方案 |
|------|----------------|
| 精度問題 | 使用 `BigDecimal`，金融級精度保證 |
| 並發問題 | 事件溯源 + ACID 事務，強一致性保證 |
| 審計需求 | 完整的事件歷史，可重播到任意時間點 |
| 擴展性 | 支持分佈式部署（PostgreSQL + Redis） |

### 與其他方案的對比

#### vs Axon Framework

| 特性 | NDS-API | Axon Framework |
|------|---------|----------------|
| 部署複雜度 | 低（嵌入式） | 高（需要消息隊列） |
| 學習曲線 | 低 | 中高 |
| 適用場景 | 中小型應用、微服務內部 | 大型分佈式系統 |
| 基礎設施 | 無需額外基礎設施 | 需要消息隊列 |

**選擇 NDS-API 如果**：
- 你不需要複雜的事件總線
- 你希望簡單的嵌入式解決方案
- 你的應用規模中等

#### vs Kafka

| 特性 | NDS-API | Kafka |
|------|---------|-------|
| 部署複雜度 | 低（單機即可） | 高（需要集群） |
| 延遲 | 低（直接數據庫） | 中（網絡開銷） |
| 適用場景 | 應用內部狀態管理 | 跨服務消息傳遞 |
| 基礎設施 | PostgreSQL + Redis | Kafka 集群 |

**選擇 NDS-API 如果**：
- 你只需要應用內部的狀態管理
- 你不需要跨服務的消息傳遞
- 你希望低延遲的狀態查詢

#### vs 自建解決方案

| 特性 | NDS-API | 自建方案 |
|------|---------|---------|
| 開發時間 | 即用 | 數月開發 |
| 測試覆蓋 | 久經考驗 | 需要自己測試 |
| 維護成本 | 社區維護 | 自己維護 |
| 功能完整性 | 完整 | 需要逐步完善 |

**選擇 NDS-API 如果**：
- 你希望快速上線
- 你不想重複造輪子
- 你需要一個經過驗證的解決方案

---

## 🏗️ 架構設計原則

### 1. 協議層設計

NDS-API 採用協議層設計，與實作完全分離：

```
┌──────────────────────┐
│   Your Application   │
└──────────────────────┘
         │
         ▼
┌──────────────────────┐
│   NDS-API            │  ← 協議層（純接口）
│   (Interfaces Only)  │
└──────────────────────┘
         │
         ▼
┌──────────────────────┐
│   NDS-Core           │  ← 實作層（可替換）
│   (Implementation)   │
└──────────────────────┘
```

**優勢**：
- 協議層零依賴，可獨立使用
- 實作層可替換（例如：不同的數據庫、不同的緩存策略）
- 測試友好（可 Mock 實作層）

### 2. 事件溯源 (Event Sourcing)

所有狀態變更都通過不可變事件記錄：

```
狀態變更
    │
    ▼
創建事件
    │
    ▼
持久化事件
    │
    ▼
投影計算新狀態
```

**優勢**：
- 完整的審計軌跡
- 可重播到任意時間點
- 無狀態丟失風險

### 3. 平台抽象

通過 `NdsPlatform` 接口支持不同運行環境：

- **Minecraft 環境**：`SpigotPlatform`（主線程執行器）
- **Spring Boot 環境**：`SpringNdsPlatform`（Spring 管理的執行器）
- **通用 Java 環境**：`StandardJavaPlatform`（ForkJoinPool）

---

## 🚀 快速開始

### Spring Boot 整合（推薦）

**注意**：Spring Boot Starter 正在開發中。目前需要手動配置。

#### 1. 添加依賴

```kotlin
dependencies {
    implementation("noie.linmimeng:noiedigitalsystem-api:2.0.0")
    // 還需要 nds-core（實作層）
}
```

#### 2. 配置 Bean

```java
@Configuration
public class NdsConfig {
    
    @Bean
    public NdsPlatform ndsPlatform() {
        return new StandardJavaPlatform();
    }
    
    @Bean
    public NdsRuntime ndsRuntime(NdsPlatform platform) {
        // 根據實際的 nds-core 實作來初始化
        // 這裡是示例
        NdsRuntime runtime = createNdsRuntime(platform);
        NdsProvider.register(runtime);
        return runtime;
    }
    
    private NdsRuntime createNdsRuntime(NdsPlatform platform) {
        // 實際實作需要根據 nds-core 的結構來調整
        throw new UnsupportedOperationException("需要實作 NdsRuntime 創建邏輯");
    }
}
```

#### 3. 使用示例

```java
@Service
public class WalletService {
    
    private final NdsRuntime runtime;
    private final AssetId walletAsset = AssetId.of(AssetScope.ACCOUNT, "wallet");
    
    public WalletService(NdsRuntime runtime) {
        this.runtime = runtime;
    }
    
    public CompletableFuture<BigDecimal> getBalance(String accountId) {
        NdsIdentity identity = NdsIdentity.of(accountId, IdentityType.ACCOUNT);
        return runtime.query().queryBalance(walletAsset, identity)
            .thenApply(result -> {
                if (result.isSuccess()) {
                    return result.data();
                }
                throw new RuntimeException("Failed to query balance: " + result.error().message());
            });
    }
    
    public CompletableFuture<NdsResult<Void>> deposit(String accountId, BigDecimal amount) {
        NdsIdentity identity = NdsIdentity.of(accountId, IdentityType.ACCOUNT);
        NdsTransaction transaction = NdsTransactionBuilder.create()
            .actor(identity)
            .asset(walletAsset)
            .delta(amount)
            .consistency(ConsistencyMode.STRONG)
            .reason("deposit")
            .build();
        
        return runtime.eventBus().publish(transaction);
    }
}
```

### 純 Java 應用

```java
public class MyApplication {
    
    public static void main(String[] args) {
        // 初始化 NDS Runtime
        NdsPlatform platform = new StandardJavaPlatform();
        NdsRuntime runtime = createNdsRuntime(platform);
        NdsProvider.register(runtime);
        
        // 使用 NDS API
        NdsIdentity account = NdsIdentity.of("user-123", IdentityType.ACCOUNT);
        AssetId wallet = AssetId.of(AssetScope.ACCOUNT, "wallet");
        
        runtime.query().queryBalance(wallet, account)
            .thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("Balance: " + result.data());
                }
            });
    }
}
```

---

## 📚 最佳實踐

### 1. 錯誤處理

**✅ 正確做法**：

```java
runtime.query().queryBalance(assetId, identity)
    .thenAccept(result -> {
        if (result.isSuccess()) {
            BigDecimal balance = result.data();
            // 使用餘額
        } else {
            // 處理業務失敗
            NdsError error = result.error();
            logger.warn("Query failed: " + error.message());
        }
    })
    .exceptionally(ex -> {
        // 處理系統錯誤
        logger.error("System error: " + ex.getMessage(), ex);
        return null;
    });
```

**❌ 錯誤做法**：

```java
// 不要直接訪問 .data() 而不檢查
BigDecimal balance = runtime.query().queryBalance(assetId, identity)
    .get()  // 阻塞！
    .data();  // 可能拋出異常！
```

### 2. 異步處理

**✅ 正確做法**：

```java
// 使用回調，不阻塞
runtime.query().queryBalance(assetId, identity)
    .thenAcceptAsync(result -> {
        // 處理結果
    }, runtime.defaultExecutor());
```

**❌ 錯誤做法**：

```java
// 不要阻塞線程
BigDecimal balance = runtime.query().queryBalance(assetId, identity)
    .get();  // 阻塞！
```

### 3. 數值計算

**✅ 正確做法**：

```java
BigDecimal price = new BigDecimal("100.50");
BigDecimal quantity = BigDecimal.valueOf(10);
BigDecimal total = price.multiply(quantity);
```

**❌ 錯誤做法**：

```java
double price = 100.50;  // 精度問題！
double quantity = 10.0;
double total = price * quantity;  // 可能產生誤差
```

---

## ❓ 常見問題

### Q1: NDS-API 需要什麼基礎設施？

**A**: NDS-API 本身是純 Java 庫，無依賴。但實作層（nds-core）需要：
- PostgreSQL（事件存儲）
- Redis（可選，用於緩存和跨服務同步）

### Q2: 可以在單機環境使用嗎？

**A**: 可以。NDS-API 支持單機部署，只需要本地 PostgreSQL 即可。

### Q3: 性能如何？

**A**: NDS-API 最初為 Minecraft 遊戲生態設計，經過數年高並發場景驗證：
- 支持數千並發請求
- 低延遲（直接數據庫操作）
- 非阻塞異步 API

### Q4: 如何處理分佈式部署？

**A**: 通過 Redis Pub/Sub 實現跨服務同步。所有服務共享同一個 PostgreSQL 數據庫和 Redis 實例。

### Q5: 與 Spring Boot 整合困難嗎？

**A**: 不困難。NDS-API 設計為平台無關，可以輕鬆整合到 Spring Boot 中。Spring Boot Starter 正在開發中。

---

## 🔗 相關資源

- **完整 API 文檔**：[README.md](./README.md)
- **Minecraft 開發者指南**：[DEVELOPER_GUIDE_TW.md](./DEVELOPER_GUIDE_TW.md)
- **GitHub Repository**：[https://github.com/Misty4119/nds-api](https://github.com/Misty4119/nds-api)

---

**Version**: 2.0.0  
**Last Updated**: 2025-01-XX

