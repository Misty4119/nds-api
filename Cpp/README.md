# NDS-API - C++ SDK

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on C++.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

This repository does not publish a prebuilt C++ package. Use the generated sources under `Cpp/src/**` in your build.

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Build with CMake

```bash
cmake -S Cpp -B Cpp/build
cmake --build Cpp/build -j
```

## Compatibility

- C++26 (CMake 3.25+ recommended)
- Protocol Buffers C++ runtime required

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`

