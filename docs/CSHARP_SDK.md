# C# SDK Guide

- Protocol spec: [`../spec/docs/PROTOCOL.md`](../spec/docs/PROTOCOL.md)
- Docs index: [`README.md`](README.md)

## Requirements

- .NET SDK 10.0+

## Installation

```bash
dotnet add package Noie.Nds.Api --version 2.0.0
```

## Key concepts

- **Identity**: `INdsIdentity`
- **Asset**: `IAssetId`
- **Event**: `INdsEvent`
- **Transaction**: `INdsTransaction`
- **Result**: `NdsResult<T>` / `NdsError`

## Build & test locally

```bash
dotnet build csharp/ --configuration Release
dotnet test csharp/ --configuration Release
```

