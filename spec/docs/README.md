# spec/docs

This directory contains documentation for the **NDS protocol** defined in `spec/proto/`.

- **Protocol spec**: [`PROTOCOL.md`](PROTOCOL.md)
- **Versioned packages (v1)**: [`V3.md`](V3.md)
- **v2 to v3 bridge (mapping)**: [`V3_BRIDGE.md`](V3_BRIDGE.md)
- **Buf config**: see `../buf.gen.yaml` and `../proto/buf.yaml`

## Code generation (local)

- **Proto source**: `spec/proto/**`
- **C# output (single source of truth in this repo)**: `csharp/src/Noie.Nds.Api.Proto`
  - Note: `spec/csharp/` is a legacy/forbidden output location and should remain empty.

