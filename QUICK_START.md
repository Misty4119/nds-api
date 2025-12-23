# NDS API 快速發布指南

## 🎯 目標

先發布 NDS API 協議層（2.0.0），搶佔協議標準，讓開發者可以：
- 提前了解 NDS 協議
- 開始開發 NDS-native 插件
- 為 NDS 生態系統做準備

## ⚡ 快速發布（3 步）

### Windows
```bash
cd nds-api
publish.bat
```

### Linux/Mac
```bash
cd nds-api
chmod +x publish.sh
./publish.sh
```

### 手動發布
```bash
# 從項目根目錄
./gradlew :nds-api:clean :nds-api:build :nds-api:publish
```

## ✅ 發布前檢查

1. **版本號**：確認 `build.gradle` 中 `version = '2.0.0'`
2. **認證信息**：確認 `gradle.properties` 中有 Repsy 認證，或設置環境變數
3. **構建測試**：執行 `./gradlew :nds-api:build` 確認無錯誤

## 📦 發布後

其他開發者可以立即使用：

```groovy
repositories {
    maven { url 'https://repo.repsy.io/mvn/linmimeng/releases' }
}

dependencies {
    compileOnly 'noie.linmimeng:noiedigitalsystem-api:2.0.0'
}
```

## 📚 相關文檔

- **詳細發布指南**：`PUBLISH.md`
- **發布檢查清單**：`RELEASE_CHECKLIST.md`
- **開發者指南**：`README.md`

## ⚠️ 重要提醒

1. **API 是協議層**：發布的只是接口定義，實際運行需要 NoieDigitalSystem 核心插件
2. **版本對應**：API 2.0.0 對應核心插件 2.0.0
3. **向後兼容**：盡量保持 API 向後兼容，重大變更需要更新 MAJOR 版本

