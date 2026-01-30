# Noie.Nds - .NET SDK for NoieDigitalSystem

[![NuGet](https://img.shields.io/nuget/v/Noie.Nds.Api.svg)](https://www.nuget.org/packages/Noie.Nds.Api/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on .NET.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
dotnet add package Noie.Nds.Api
```

Or for just the abstractions:

```bash
dotnet add package Noie.Nds.Api.Abstractions
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Governance & audit hooks**: Policy (`Noie.Nds.Api.Policy`) and Audit/Rationale (`Noie.Nds.Api.Audit`)
- **Type safety**: nullable reference types enabled
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **AOT-friendly**: trimming analyzers enabled

## Quick Start

### Identity

```csharp
using Noie.Nds.Api.Identity;

// Create player identity
var player = NdsIdentity.Of("550e8400-e29b-41d4-a716-446655440000", IdentityType.Player);

// Parse from string
var identity = NdsIdentity.FromString("PLAYER:550e8400-e29b-41d4-a716-446655440000");
```

### Asset

```csharp
using Noie.Nds.Api.Asset;

// Create asset IDs
var coins = AssetId.Player("coins");
var bossHp = AssetId.Server("world_boss_hp");

// Parse from string
var asset = AssetId.FromString("player:gold");
```

### Result Pattern

```csharp
using Noie.Nds.Api.Result;

// Create results
var success = NdsResult<int>.Success(100);
var failure = NdsResult<int>.Failure("INSUFFICIENT_BALANCE", "Not enough coins");

// Functional chaining
var result = success
    .Map(x => x * 2)
    .OnSuccess(x => Console.WriteLine($"Value: {x}"))
    .OnFailure(e => Console.WriteLine($"Error: {e.Message}"));

// Pattern matching
var message = result.Match(
    onSuccess: x => $"Got {x}",
    onFailure: e => $"Failed: {e.Code}"
);
```

### Context

```csharp
using Noie.Nds.Api.Context;

// Create context for tracing
var context = NdsContext.Create();
Console.WriteLine($"TraceId: {context.TraceId}");

// Add metadata
var enriched = context.WithMeta("user", "player123");
```

### Policy + Rationale

```csharp
using Noie.Nds.Api.Policy;
using Noie.Nds.Api.Audit;

var policy = NdsPolicy.Of("shop-default-policy-v1", "quota");
var rationale = NdsRationale.Of(
    source: "human_admin",
    thoughtPath: new[] { "manual_review", "approved" }
);
```

## Packages

| Package | Description |
|---------|-------------|
| `Noie.Nds.Api.Abstractions` | Core interfaces and types |
| `Noie.Nds.Api` | Full implementation with Proto support |

## Compatibility

- .NET 10 (`net10.0`)

## Related Projects

- [nds-api](https://github.com/Misty4119/nds-api) - Protocol + SDKs monorepo

## License

Apache License 2.0
