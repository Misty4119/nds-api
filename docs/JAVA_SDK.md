# Java SDK Guide

- Protocol spec: [`../spec/docs/PROTOCOL.md`](../spec/docs/PROTOCOL.md)
- Docs index: [`README.md`](README.md)

## Requirements

- Java 21+

## Installation

### Maven

```xml
<dependency>
  <groupId>io.github.misty4119</groupId>
  <artifactId>noiedigitalsystem-api</artifactId>
  <version>2.0.0</version>
</dependency>
```

### Gradle (Kotlin)

```kotlin
dependencies {
  implementation("io.github.misty4119:noiedigitalsystem-api:2.0.0")
}
```

## Key concepts

- **Identity**: who performs an action (`NdsIdentity`)
- **Asset**: what is being changed (`AssetId`)
- **Event**: immutable record of a state change (`NdsEvent`)
- **Transaction**: an event with asset delta semantics (`NdsTransaction`)
- **Result**: success/failure envelope (`NdsResult`)

## API usage

This repository focuses on **protocol + SDK types**. Runtime integration depends on the platform implementation.

## Build locally

```bash
./gradlew :java:build
```

