# NDS-API - Go SDK

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on Go.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
go get github.com/misty4119/nds-api/Go@v3.0.4
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Decimal

```go
package main

import (
	"fmt"

	"github.com/misty4119/nds-api/Go/nds/common"
)

func main() {
	d := &common.Decimal{Value: "1.23", Scale: 2}
	fmt.Println(d.GetValue())
}
```

## Packages

| Module | Description |
|--------|-------------|
| `github.com/misty4119/nds-api/Go` | Go protobuf DTOs generated from `spec/proto/**` |

## Compatibility

- Go 1.25.x

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`


