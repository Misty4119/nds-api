# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- **[docs]** Refresh repository onboarding docs (README, docs index, protocol docs).
- **[docs]** Ensure documentation is published (adjust `.gitignore`).
- **[spec/java/csharp]** Normalize comment language and apply structured, indexed comment format.

## [2.0.0] - 2026-01-26

### Added

- **[spec]** Protocol Buffers specification
  - `common.proto` - common types (Decimal, NdsResult, NdsError)
  - `identity.proto` - identities
  - `asset.proto` - assets
  - `context.proto` - context
  - `event.proto` - events
  - `transaction.proto` - transactions
  - `projection.proto` - projections
  - `query.proto` - query service

- **[java]** Java SDK
  - Adapter layer (Proto ↔ domain conversions)
  - Event-sourcing primitives
  - Async APIs based on `CompletableFuture`

- **[csharp]** C# SDK
  - .NET 10 support
  - Core abstractions
  - Adapter layer
  - 19 unit tests

### Changed

- **[architecture]** Restructured into a monorepo
  - `spec/` - protocol spec (L0)
  - `java/` - Java SDK (L1)
  - `csharp/` - C# SDK (L1)

- **[csharp]** Upgrade to .NET 10 LTS
  - C# 14 language version
  - AOT compatibility flags

### Deprecated

- **[java]** `NoieDigitalSystemAPI`
  - Use `NdsRuntime` instead

---

## [1.x.x] - Legacy

Legacy API versions. Please upgrade to 2.0.0.
