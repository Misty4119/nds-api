# NDS-API - Rust SDK

[![crates.io](https://img.shields.io/crates/v/noie-nds-api.svg)](https://crates.io/crates/noie-nds-api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on Rust.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
cargo add noie-nds-api@3.0.2
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Decimal

```rust
use noie_nds_api::nds::common::Decimal;

let d = Decimal {
    value: "1.23".to_string(),
    scale: 2,
};

println!("{}", d.value);
```

## Packages

| Crate | Description |
|-------|-------------|
| `noie-nds-api` | Rust protobuf DTOs (prost) |

## Compatibility

- Rust 1.93.0 (edition 2021)

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`


