# NDS-API - TypeScript SDK

[![npm](https://img.shields.io/npm/v/@misty4119/nds-api.svg)](https://www.npmjs.com/package/@misty4119/nds-api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on TypeScript.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
npm i @misty4119/nds-api@3.0.4
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Decimal

```ts
import { ndsCommon } from "@misty4119/nds-api";

const d: ndsCommon.Decimal = { value: "1.23", scale: 2 };
console.log(d.value);
```

## Packages

| Package | Description |
|---------|-------------|
| `@misty4119/nds-api` | TypeScript protobuf DTOs (protobuf-ts runtime) |

## Compatibility

- Node.js 24 (Active LTS) / 22 (Maintenance LTS)
- TypeScript 5.9.3

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`


