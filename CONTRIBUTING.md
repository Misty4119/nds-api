# Contributing

Thanks for your interest in contributing to **NDS-API**.

## Repository policies

### Language policy

- **English is required** for all documentation and code comments **except** `*_TW.md`, which must be **Traditional Chinese**.

### Comment policy (indexed + structured)

Code comments are treated as **structured context**. When adding or updating comments (`//`, `/* */`, JavaDoc, C# XML docs, proto comments), prefer:

- **Indexing**: include an identifier (e.g., `[Index] NDS-JAVA-...`, `[Index] NDS-PROTO-...`)
- **Tags**: use concise tags such as `[Constraint]`, `[Behavior]`, `[Semantic]`, `[Trace]`, `[Security]`, `[Performance]`, `[Audit]`, `[Sensitive]`
- **Tone**: avoid casual wording; keep comments mechanical and unambiguous

## Development setup

### Requirements

| Tool | Version | Notes |
|------|---------|-------|
| Java | 21+ | Java SDK |
| .NET SDK | 10.0+ | C# SDK |
| Buf CLI | 1.64+ | Proto lint / codegen |
| Git | 2.40+ | Version control |

### First-time setup

```bash
git clone https://github.com/Misty4119/nds-api.git
cd nds-api

buf lint spec/proto
./gradlew :java:build
dotnet build csharp/
./gradlew :java:test && dotnet test csharp/
```

## Commit convention

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

### Types

- `feat`: new feature
- `fix`: bug fix
- `docs`: documentation
- `style`: formatting only
- `refactor`: refactoring
- `test`: tests
- `chore`: maintenance

### Scopes

- `spec`: protocol / proto
- `java`: Java SDK
- `csharp`: C# SDK
- `ci`: CI/CD

### Examples

```
feat(spec): add TransactionStatus enum to transaction.proto
feat(java): implement TransactionAdapter for proto conversion
feat(csharp): add INdsTransaction abstraction
chore(ci): update .NET SDK to 10.0
```

## Pull requests

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m "feat(scope): ..."`).
4. Push (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Code style

### Java

- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Prefer clear, immutable domain types

### C#

- Follow Microsoft’s [C# coding conventions](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/coding-style/coding-conventions)
- Keep public APIs well-documented and null-safe

### Proto

- Follow the [Buf Style Guide](https://buf.build/docs/best-practices/style-guide)
- `buf lint spec/proto` must pass

## Testing

- New features should include tests
- CI must pass before merge

## Reporting issues

Please include:

1. What happened
2. Steps to reproduce
3. Expected behavior
4. Actual behavior
5. Environment (OS, Java/.NET/Buf versions)
