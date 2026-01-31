# NDS-API - C++ SDK

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on C++.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

This repository does not publish a prebuilt C++ package.
Consume it via **Git + CMake FetchContent** (recommended), or vendor the generated sources under `Cpp/src/**`.

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

### Consume with FetchContent

Add this to your CMake project:

```cmake
include(FetchContent)
FetchContent_Declare(nds_api
  GIT_REPOSITORY https://github.com/Misty4119/nds-api.git
  GIT_TAG v3.0.4
)
FetchContent_MakeAvailable(nds_api)

add_executable(app main.cpp)
target_link_libraries(app PRIVATE noie_nds_api_proto)
```

## Compatibility

- C++26 (CMake 3.25+ recommended)
- Protocol Buffers C++ runtime required
  - Default: fetched automatically (see `NDS_API_PROTOBUF_PROVIDER=fetch`)

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`

