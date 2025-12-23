# NDS API 發布檢查清單

## 📋 發布前檢查

### 1. 代碼完整性檢查
- [ ] 所有 API 接口方法都有完整的 JavaDoc
- [ ] 模型類使用 BigDecimal（不使用 double）
- [ ] 所有接口方法都返回 CompletableFuture
- [ ] 沒有編譯錯誤
- [ ] 沒有未使用的導入

### 2. 版本號檢查
- [ ] `build.gradle` 中的 `version` 已更新
- [ ] 版本號符合語義化版本規範（MAJOR.MINOR.PATCH）
- [ ] 如果是重大變更，已更新 MAJOR 版本號

### 3. 依賴檢查
- [ ] 所有依賴都是 `compileOnly`（不打包到 JAR）
- [ ] Paper API 版本正確
- [ ] 沒有不必要的依賴

### 4. 文檔檢查
- [ ] README.md 已更新
- [ ] API 使用示例正確
- [ ] Maven 倉庫地址正確
- [ ] 版本號在文檔中已更新

### 5. 構建測試
- [ ] 執行 `./gradlew :nds-api:build` 成功
- [ ] 生成的 JAR 文件可以正常使用
- [ ] 檢查生成的 POM 文件內容正確

### 6. 發布配置檢查
- [ ] Repsy 認證信息已配置（gradle.properties）
- [ ] Maven 倉庫 URL 正確
- [ ] artifactId 和 groupId 正確

## 🚀 發布步驟

1. **更新版本號**
   ```bash
   # 編輯 build.gradle，更新 version
   ```

2. **構建項目**
   ```bash
   cd nds-api
   ./gradlew clean build
   ```

3. **檢查構建結果**
   ```bash
   # 檢查 build/libs/ 目錄下的 JAR 文件
   ls -lh build/libs/
   ```

4. **發布到 Maven 倉庫**
   ```bash
   ./gradlew :nds-api:publish
   ```

5. **驗證發布**
   - 訪問 Repsy 倉庫查看是否上傳成功
   - 等待幾分鐘讓 Maven 索引更新
   - 嘗試在測試項目中引用新版本

## 📝 發布後任務

- [ ] 更新 GitHub 發布說明
- [ ] 通知社區新版本發布
- [ ] 更新官方文檔網站（如果有）

## ⚠️ 注意事項

1. **版本號一旦發布不可修改**：如果發布錯誤，必須發布新版本
2. **重大變更需要更新 MAJOR 版本**：破壞性變更需要謹慎
3. **保持向後兼容**：盡量保持 API 向後兼容，使用 default 方法添加新功能
4. **測試依賴引用**：發布後在測試項目中驗證依賴可以正常引用

