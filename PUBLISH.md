# NDS API 發布指南

## 🎯 發布目標

先發布 NDS API 協議層，讓其他開發者可以：
1. 提前了解和使用 NDS 協議
2. 開始開發 NDS-native 插件
3. 為 NDS 生態系統做準備

即使核心實現（NoieDigitalSystem）尚未完善，API 協議已經穩定，可以獨立發布。

### ⚠️ 重要說明

**這不是發布到 SpigotMC，而是發布到 Maven 倉庫！**

- ✅ **目的**：宣告協議標準，讓開發者可以引用 API
- ✅ **效果**：其他插件開發者可以開始使用 NDS API 開發插件
- ❌ **不能直接運行**：這個 JAR 不能放在伺服器的 `plugins/` 資料夾
- ❌ **不是插件**：沒有 `plugin.yml`，只是 API 庫

**實際可運行的插件（`nds-bukkit`）將在未來發布到 SpigotMC。**

## 📦 發布信息

### Maven 坐標
```xml
<groupId>noie.linmimeng</groupId>
<artifactId>noiedigitalsystem-api</artifactId>
<version>2.0.0</version>
```

### 倉庫地址
```
https://repo.repsy.io/mvn/linmimeng/releases
```

## 🔧 快速發布

### 方法 1: 使用 Gradle 命令（推薦）

```bash
# 1. 進入 API 模組目錄
cd nds-api

# 2. 構建項目
../gradlew clean build

# 3. 發布到 Maven 倉庫
../gradlew :nds-api:publish
```

### 方法 2: 從根目錄執行

```bash
# 從項目根目錄
./gradlew :nds-api:clean :nds-api:build :nds-api:publish
```

## ✅ 發布前必做

1. **檢查版本號**
   - 編輯 `nds-api/build.gradle`
   - 確認 `version = '2.0.0'` 正確

2. **檢查認證信息**
   - 確認 `nds-api/gradle.properties` 中有 Repsy 認證信息
   - 或設置環境變數 `REPSY_USERNAME` 和 `REPSY_PASSWORD`

3. **構建測試**
   ```bash
   ./gradlew :nds-api:build
   ```
   - 確認沒有編譯錯誤
   - 檢查 `nds-api/build/libs/` 目錄

## 🔍 驗證發布

### 1. 檢查 Repsy 倉庫
訪問：https://repo.repsy.io/mvn/linmimeng/releases/noie/linmimeng/noiedigitalsystem-api/

### 2. 測試依賴引用

創建測試項目 `build.gradle`：
```groovy
repositories {
    maven { url 'https://repo.repsy.io/mvn/linmimeng/releases' }
}

dependencies {
    compileOnly 'noie.linmimeng:noiedigitalsystem-api:2.0.0'
}
```

執行：
```bash
./gradlew dependencies --configuration compileClasspath
```

應該能看到 `noiedigitalsystem-api:2.0.0` 被正確解析。

## 📚 發布後文檔

發布後，其他開發者可以：

1. **添加依賴**
   ```groovy
   repositories {
       maven { url 'https://repo.repsy.io/mvn/linmimeng/releases' }
   }
   dependencies {
       compileOnly 'noie.linmimeng:noiedigitalsystem-api:2.0.0'
   }
   ```

2. **開始開發**
   - 參考 `README.md` 中的開發指南
   - 使用 `NoieDigitalSystemAPI` 接口
   - 注意：需要等待 NoieDigitalSystem 核心插件發布後才能運行

## ⚠️ 重要提醒

1. **API 是協議層，不是實現**
   - 發布的只是接口定義
   - 實際運行需要 NoieDigitalSystem 核心插件

2. **版本兼容性**
   - API 版本 2.0.0 對應核心插件版本 2.0.0
   - 確保版本號一致

3. **向後兼容**
   - 盡量保持 API 向後兼容
   - 重大變更需要更新 MAJOR 版本號

## 🐛 常見問題

### Q: 發布失敗，認證錯誤
A: 檢查 `gradle.properties` 或環境變數中的 Repsy 認證信息

### Q: 發布成功但找不到依賴
A: 等待幾分鐘讓 Maven 索引更新，或清除本地 Maven 緩存

### Q: 版本號衝突
A: 如果版本已存在，需要更新版本號後重新發布

## 📞 支持

如有問題，請聯繫：
- GitHub Issues: [項目地址]
- 文檔: README.md

