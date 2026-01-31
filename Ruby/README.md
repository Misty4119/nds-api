# NDS-API - Ruby SDK

[![RubyGems](https://img.shields.io/gem/v/noie-nds-api.svg)](https://rubygems.org/gems/noie-nds-api)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](../LICENSE)

Protocol SDK for **NDS-API** (NoieDigitalSystem) on Ruby.

- Protocol spec: `../spec/proto/`
- Repo docs index: [`../docs/README.md`](../docs/README.md)

## Installation

```bash
gem install noie-nds-api -v 3.0.3
```

## Features

- **Protocol types**: Identity / Asset / Event / Transaction / Result / Context
- **Proto compatibility**: matches the NDS Protocol Buffers specification
- **Runtime-agnostic**: no Bukkit/Paper, no database, no network stack dependencies

## Quick Start

### Decimal

```ruby
require "noie_nds_api"

d = Nds::Common::Decimal.new(value: "1.23", scale: 2)
puts d.value
```

## Packages

| Gem | Description |
|-----|-------------|
| `noie-nds-api` | Ruby protobuf DTOs (google-protobuf runtime) |

## Compatibility

- Ruby 4.0.1

## Notes (generated code)

- Protobuf DTOs are generated from `../spec/proto/**` via Buf.
- Generation template: `../spec/buf.gen.yaml`


