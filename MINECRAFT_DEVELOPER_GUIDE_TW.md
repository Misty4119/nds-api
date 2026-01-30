# Minecraft 開發者指南

本指南說明如何在 Minecraft 插件環境（Spigot/Paper/Folia）中整合 NDS-API。

通用概念與多語言用法請見 `DEVELOPER_GUIDE_TW.md`。

## 快速規則

- 所有經濟數值使用 `BigDecimal`。
- 不要阻塞伺服器主執行緒（避免對 Future 使用 `.get()`/`.join()`）。
- 不要快取餘額，除非你有明確的失效策略。
- Payload 只能放入可序列化的基本型別/集合（不得放 Bukkit 物件）。

## 依賴設定（Java）

請以 provided/compileOnly 方式引入 NDS-API。

**Gradle (Kotlin)**

```kotlin
repositories { mavenCentral() }
dependencies { compileOnly("io.github.misty4119:noiedigitalsystem-api:<VERSION>") }
```

**Maven**

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version><!-- 參考 Maven Central --></version>
  <scope>provided</scope>
</dependency>
```

## 取得 runtime 實例

伺服器會在執行時提供 runtime 實作。你的插件可以這樣取得：

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

public final class MyPlugin extends JavaPlugin {
  private NdsRuntime runtime;

  @Override
  public void onEnable() {
    if (!NdsProvider.isInitialized()) {
      getLogger().severe("NDS runtime 尚未初始化。");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    runtime = NdsProvider.get();
  }
}
```

## 身份與資產

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;

NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

## 非同步查詢餘額

```java
runtime.query().queryBalance(coins, playerIdentity)
  .thenAcceptAsync(result -> {
    if (result.isSuccess()) {
      BigDecimal balance = result.data();
      player.sendMessage("餘額: " + balance);
    } else {
      player.sendMessage("查詢失敗: " + result.error().message());
    }
  }, runtime.defaultExecutor());
```

## 套用變更（交易模式）

```java
import java.math.BigDecimal;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

NdsTransaction tx = NdsTransactionBuilder.create()
  .actor(playerIdentity)
  .asset(coins)
  .delta(BigDecimal.valueOf(-100)) // 負數 = 扣款
  .consistency(ConsistencyMode.STRONG)
  .reason("SHOP_PURCHASE")
  .build();

runtime.eventBus().publish(tx)
  .thenAcceptAsync(result -> {
    if (result.isSuccess()) {
      player.sendMessage("交易成功");
    } else {
      player.sendMessage("交易失敗: " + result.error().message());
    }
  }, runtime.defaultExecutor());
```

## Bukkit/Folia 執行緒注意事項

- 除非伺服器或 runtime 文件明確說明，否則請把 Bukkit API 視為非執行緒安全。
- 建議使用 runtime 提供的 executor 來執行回調，並在需要時將 Bukkit 操作切回正確的執行緒/排程器。

## 連結

- 通用指南：`DEVELOPER_GUIDE_TW.md`
- 遷移計畫：`nds_migration_next_gen_vault_replacement_plan_for_minecraft.md`
- 協議文件：`spec/docs/README.md`

---

Last updated: 2026-01-31

