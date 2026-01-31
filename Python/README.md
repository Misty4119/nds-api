# NDS-API - Python SDK

[![PyPI](https://img.shields.io/pypi/v/noie-nds-api.svg)](https://pypi.org/project/noie-nds-api/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on Python.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
pip install noie-nds-api==3.0.3
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Decimal

```python
from nds.common import common_pb2

d = common_pb2.Decimal(value="1.23", scale=2)
print(d.value)
```

## Packages

| Package | Description |
|---------|-------------|
| `noie-nds-api` | Python protobuf DTOs (`*_pb2.py`) |

## Compatibility

- Python 3.14

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`


