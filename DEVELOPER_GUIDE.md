# Developer Guide

This guide is for developers integrating NDS-API in Java or .NET applications.

## Install

### Java

Use the latest published version shown on Maven Central.

```kotlin
repositories { mavenCentral() }
dependencies { implementation("io.github.misty4119:noiedigitalsystem-api:<VERSION>") }
```

### .NET (C#)

Use the latest published version shown on NuGet.

```bash
dotnet add package Noie.Nds.Api
```

If you only need interfaces and core types:

```bash
dotnet add package Noie.Nds.Api.Abstractions
```

## Core concepts

### Identity

An identity represents the principal that performs an action (player/system/AI/external).

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

### AssetId

An asset id identifies what value is being modified, independent of storage.

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

### Exact amounts

- Java: use `BigDecimal`
- C#: use `decimal`

If you work with v3 fixed-point protocol amounts, use `Money` (units+nanos) and convert exactly.

**C# example**

```csharp
using Nds.Ledger.V1;
using Noie.Nds.Api.Adapter;

Money money = MoneyAdapter.ToProto("NDS", 100.5m);
decimal amount = MoneyAdapter.FromProto(money);
```

## Result handling

Runtime implementations typically return structured results. Always check success and handle failures explicitly.

**C#**

```csharp
using Noie.Nds.Api.Result;

var ok = NdsResult<int>.Success(1);
var fail = NdsResult<int>.Failure("VALIDATION_INVALID_ARGUMENT", "bad input");
```

## Protocol documentation

- Protocol docs entry: `spec/docs/README.md`

---

Last updated: 2026-01-31

