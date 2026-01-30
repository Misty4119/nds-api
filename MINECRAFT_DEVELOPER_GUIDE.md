# Minecraft Developer Guide

This guide shows how to integrate NDS-API in Minecraft plugin environments (Spigot/Paper/Folia).

For general concepts and multi-language usage, see `DEVELOPER_GUIDE.md`.

## Quick rules

- Use `BigDecimal` for all economic values.
- Do not block the server thread (avoid `.get()`/`.join()` on futures).
- Do not cache balances unless you have a clear invalidation strategy.
- Payloads must contain only serializable primitives/collections (no Bukkit objects).

## Dependency setup (Java)

Add NDS-API as a provided/compileOnly dependency.

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
  <version><!-- see Maven Central --></version>
  <scope>provided</scope>
</dependency>
```

## Getting a runtime instance

Your server will provide a runtime implementation at runtime. In your plugin:

```java
import noie.linmimeng.noiedigitalsystem.api.NdsProvider;
import noie.linmimeng.noiedigitalsystem.api.NdsRuntime;

public final class MyPlugin extends JavaPlugin {
  private NdsRuntime runtime;

  @Override
  public void onEnable() {
    if (!NdsProvider.isInitialized()) {
      getLogger().severe("NDS runtime is not initialized.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    runtime = NdsProvider.get();
  }
}
```

## Identity and assets

```java
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;

NdsIdentity playerIdentity = NdsIdentity.fromString(player.getUniqueId().toString());
AssetId coins = AssetId.of(AssetScope.PLAYER, "coins");
```

## Query balance (async)

```java
runtime.query().queryBalance(coins, playerIdentity)
  .thenAcceptAsync(result -> {
    if (result.isSuccess()) {
      BigDecimal balance = result.data();
      player.sendMessage("Balance: " + balance);
    } else {
      player.sendMessage("Query failed: " + result.error().message());
    }
  }, runtime.defaultExecutor());
```

## Apply a value change (transaction pattern)

```java
import java.math.BigDecimal;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransaction;
import noie.linmimeng.noiedigitalsystem.api.transaction.NdsTransactionBuilder;
import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

NdsTransaction tx = NdsTransactionBuilder.create()
  .actor(playerIdentity)
  .asset(coins)
  .delta(BigDecimal.valueOf(-100))  // negative = deduct
  .consistency(ConsistencyMode.STRONG)
  .reason("SHOP_PURCHASE")
  .build();

runtime.eventBus().publish(tx)
  .thenAcceptAsync(result -> {
    if (result.isSuccess()) {
      player.sendMessage("Purchase OK");
    } else {
      player.sendMessage("Purchase failed: " + result.error().message());
    }
  }, runtime.defaultExecutor());
```

## Bukkit/Folia thread safety notes

- Treat Bukkit API as not thread-safe unless your server/runtime documents otherwise.
- Run Minecraft API calls on the appropriate executor/scheduler for your environment.
- Prefer using the runtime-provided executor for callbacks, and isolate Bukkit interactions when needed.

## Links

- General guide: `DEVELOPER_GUIDE.md`
- Migration plan: `nds_migration_next_gen_vault_replacement_plan_for_minecraft.md`
- Protocol docs: `spec/docs/README.md`

---

Last updated: 2026-01-31

