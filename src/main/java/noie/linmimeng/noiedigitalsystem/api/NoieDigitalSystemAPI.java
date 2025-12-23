package noie.linmimeng.noiedigitalsystem.api;

import noie.linmimeng.noiedigitalsystem.model.Digital;
import noie.linmimeng.noiedigitalsystem.model.PlayerDigital;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * NoieDigitalSystem API - Next-Generation Economy Protocol (NGEP)
 * 
 * <p><b>核心定位：協議層（Protocol Layer），非功能層</b></p>
 * 
 * <p>NDS 不是另一個經濟插件，而是 Minecraft 生態中的「經濟/狀態協議層」。
 * 就像 HTTP 對 Web、JDBC 對資料庫，NDS 提供統一的狀態管理協議。</p> 
 * 
 * <h2>設計原則：</h2>
 * <ul>
 *   <li><b>單一真相來源（Single Source of Truth）</b>：所有經濟狀態由 NDS 管理</li>
 *   <li><b>非同步優先（Async-first）</b>：所有操作返回 CompletableFuture，禁止阻塞</li>
 *   <li><b>協議優先，功能為輔</b>：核心狀態層獨立，應用層（Web/商店/拍賣）可選</li>
 *   <li><b>安全與一致性</b>：BigDecimal 精度、交易原子性、跨服同步</li>
 * </ul>
 * 
 * <h2>使用範例：</h2>
 * <pre>{@code
 * // 取得 API 實例
 * Plugin ndsPlugin = getServer().getPluginManager().getPlugin("NoieDigitalSystem");
 * if (!(ndsPlugin instanceof NoieDigitalSystem)) {
 *     return;
 * }
 * NoieDigitalSystemAPI api = ((NoieDigitalSystem) ndsPlugin).getAPI();
 * 
 * // 非同步操作（推薦）
 * api.getPlayerDigitalAmount(uuid, "coins")
 *     .thenAccept(balance -> {
 *         // 處理餘額
 *     })
 *     .exceptionally(ex -> {
 *         getLogger().severe("Error: " + ex.getMessage());
 *         return null;
 *     });
 * }</pre>
 * 
 * <p><b>注意：</b>所有方法都是非同步的，請勿在主線程使用 {@code .get()} 阻塞操作。</p>
 * 
 * @author NoieDigitalSystem Team
 * @version 2.0.0
 * @since 2.0.0
 */
public interface NoieDigitalSystemAPI {

    // ==================== Server Digital Operations ====================
    // 全服變數操作（世界事件、Boss HP 等）
    // 所有方法使用 BigDecimal 確保精度，返回 CompletableFuture 實現非同步
    
    /**
     * 取得全服 Digital 數值
     * 
     * @param digitalName Digital 名稱（小寫+下劃線，如 "world_boss_hp"）
     * @return CompletableFuture 包含數值，不存在時返回 BigDecimal.ZERO
     */
    CompletableFuture<BigDecimal> getServerDigitalAmount(String digitalName);
    CompletableFuture<Void> setServerDigitalAmount(String digitalName, BigDecimal amount);
    CompletableFuture<Void> giveServerDigital(String digitalName, BigDecimal amount);
    CompletableFuture<Boolean> takeServerDigital(String digitalName, BigDecimal amount);
    CompletableFuture<Void> createServerDigital(String digitalName, BigDecimal initialAmount, BigDecimal limit);
    CompletableFuture<Void> removeServerDigital(String digitalName);
    CompletableFuture<Void> renameServerDigital(String oldName, String newName);
    CompletableFuture<BigDecimal> getServerDigitalLimit(String digitalName);
    CompletableFuture<Void> setServerDigitalLimit(String digitalName, BigDecimal limit);
    CompletableFuture<Map<String, Digital>> getAllServerDigitals();

    // Server Digital Operations - double 版本（向後兼容）
    default CompletableFuture<Double> getServerDigitalAmountDouble(String digitalName) {
        return getServerDigitalAmount(digitalName).thenApply(BigDecimal::doubleValue);
    }
    default CompletableFuture<Void> setServerDigitalAmount(String digitalName, double amount) {
        return setServerDigitalAmount(digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Void> giveServerDigital(String digitalName, double amount) {
        return giveServerDigital(digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Boolean> takeServerDigital(String digitalName, double amount) {
        return takeServerDigital(digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Void> createServerDigital(String digitalName, double initialAmount, double limit) {
        return createServerDigital(digitalName, BigDecimal.valueOf(initialAmount), 
                limit > 0 ? BigDecimal.valueOf(limit) : null);
    }
    default CompletableFuture<Double> getServerDigitalLimitDouble(String digitalName) {
        return getServerDigitalLimit(digitalName).thenApply(bd -> bd != null ? bd.doubleValue() : -1.0);
    }
    default CompletableFuture<Void> setServerDigitalLimit(String digitalName, double limit) {
        return setServerDigitalLimit(digitalName, limit > 0 ? BigDecimal.valueOf(limit) : null);
    }

    // ==================== Player Digital Operations ====================
    // 玩家私有資源操作（最常用）
    // 所有方法使用 BigDecimal 確保精度，返回 CompletableFuture 實現非同步
    
    /**
     * 取得玩家 Digital 餘額
     * 
     * <p>如果 Digital 不存在，會自動創建（使用 Global Player Digital 的初始值，或預設 0）</p>
     * 
     * @param playerUUID 玩家 UUID
     * @param digitalName Digital 名稱（小寫+下劃線，如 "coins", "gold"）
     * @return CompletableFuture 包含餘額，不存在時返回 BigDecimal.ZERO
     */
    CompletableFuture<BigDecimal> getPlayerDigitalAmount(UUID playerUUID, String digitalName);
    CompletableFuture<Void> setPlayerDigitalAmount(UUID playerUUID, String digitalName, BigDecimal amount);
    CompletableFuture<Void> givePlayerDigital(UUID playerUUID, String digitalName, BigDecimal amount);
    CompletableFuture<Boolean> takePlayerDigital(UUID playerUUID, String digitalName, BigDecimal amount);
    CompletableFuture<Void> createPlayerDigital(UUID playerUUID, String digitalName, BigDecimal initialAmount, BigDecimal limit);
    CompletableFuture<Void> removePlayerDigital(UUID playerUUID, String digitalName);
    CompletableFuture<Void> renamePlayerDigital(UUID playerUUID, String oldName, String newName);
    CompletableFuture<BigDecimal> getPlayerDigitalLimit(UUID playerUUID, String digitalName);
    CompletableFuture<Void> setPlayerDigitalLimit(UUID playerUUID, String digitalName, BigDecimal limit);
    CompletableFuture<Map<String, PlayerDigital>> getAllPlayerDigitals(UUID playerUUID);

    // Player Digital Operations - double 版本（向後兼容）
    default CompletableFuture<Double> getPlayerDigitalAmountDouble(UUID playerUUID, String digitalName) {
        return getPlayerDigitalAmount(playerUUID, digitalName).thenApply(BigDecimal::doubleValue);
    }
    default CompletableFuture<Void> setPlayerDigitalAmount(UUID playerUUID, String digitalName, double amount) {
        return setPlayerDigitalAmount(playerUUID, digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Void> givePlayerDigital(UUID playerUUID, String digitalName, double amount) {
        return givePlayerDigital(playerUUID, digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Boolean> takePlayerDigital(UUID playerUUID, String digitalName, double amount) {
        return takePlayerDigital(playerUUID, digitalName, BigDecimal.valueOf(amount));
    }
    default CompletableFuture<Void> createPlayerDigital(UUID playerUUID, String digitalName, double initialAmount, double limit) {
        return createPlayerDigital(playerUUID, digitalName, BigDecimal.valueOf(initialAmount),
                limit > 0 ? BigDecimal.valueOf(limit) : null);
    }
    default CompletableFuture<Double> getPlayerDigitalLimitDouble(UUID playerUUID, String digitalName) {
        return getPlayerDigitalLimit(playerUUID, digitalName).thenApply(bd -> bd != null ? bd.doubleValue() : -1.0);
    }
    default CompletableFuture<Void> setPlayerDigitalLimit(UUID playerUUID, String digitalName, double limit) {
        return setPlayerDigitalLimit(playerUUID, digitalName, limit > 0 ? BigDecimal.valueOf(limit) : null);
    }

    // ==================== Global Player Digital Operations ====================
    // 全域玩家 Digital 定義（系統設定層）
    // 創建後，所有玩家自動擁有該 Digital（使用初始值）
    
    /**
     * 創建全域玩家 Digital（所有玩家共享定義）
     * 
     * <p>用於定義通用貨幣（如 "coins", "gold"），創建後所有玩家自動擁有。</p>
     * <p>如果 Digital 已存在，此操作不會覆蓋現有設定。</p>
     * 
     * @param digitalName Digital 名稱（小寫+下劃線）
     * @param initialAmount 新玩家的初始金額
     * @param limit 上限（null 表示無上限）
     * @return CompletableFuture 完成時表示創建成功
     */
    CompletableFuture<Void> createGlobalPlayerDigital(String digitalName, BigDecimal initialAmount, BigDecimal limit);
    CompletableFuture<Void> removeGlobalPlayerDigital(String digitalName);
    CompletableFuture<Map<String, Digital>> getAllGlobalPlayerDigitals();
    CompletableFuture<Boolean> isGlobalPlayerDigital(String digitalName);
    
    // Global Player Digital Operations - double 版本（向後兼容）
    default CompletableFuture<Void> createGlobalPlayerDigital(String digitalName, double initialAmount, double limit) {
        return createGlobalPlayerDigital(digitalName, BigDecimal.valueOf(initialAmount),
                limit > 0 ? BigDecimal.valueOf(limit) : null);
    }

    // ==================== Digital Metadata Operations ====================
    // Digital 元數據管理（用於配置 Digital 行為）
    
    /**
     * 創建 Digital 元數據
     * @param digitalName Digital 名稱
     * @param type Digital 類型（"SERVER", "PLAYER_GLOBAL", "PLAYER"）
     * @param playerUUID 玩家 UUID（僅 PLAYER 類型需要，其他為 null）
     * @param removeOnZero 是否在歸零時移除
     * @param protectionMode 保護模式（初始為0時是否開啟保護）
     * @return CompletableFuture 完成時表示創建成功
     */
    CompletableFuture<Void> createDigitalMetadata(String digitalName, String type, UUID playerUUID, 
                                                  boolean removeOnZero, boolean protectionMode);
    
    /**
     * 獲取 Digital 元數據
     * @param digitalName Digital 名稱
     * @param type Digital 類型
     * @param playerUUID 玩家 UUID（僅 PLAYER 類型需要）
     * @return CompletableFuture 包含元數據，不存在時返回 null
     */
    CompletableFuture<DigitalMetadata> getDigitalMetadata(String digitalName, String type, UUID playerUUID);
    
    /**
     * 更新 Digital 元數據
     * @param digitalName Digital 名稱
     * @param type Digital 類型
     * @param playerUUID 玩家 UUID（僅 PLAYER 類型需要）
     * @param removeOnZero 是否在歸零時移除（null 表示不更新）
     * @param protectionMode 保護模式（null 表示不更新）
     * @return CompletableFuture 完成時表示更新成功
     */
    CompletableFuture<Void> updateDigitalMetadata(String digitalName, String type, UUID playerUUID,
                                                 Boolean removeOnZero, Boolean protectionMode);
    
    /**
     * 刪除 Digital 元數據
     * @param digitalName Digital 名稱
     * @param type Digital 類型
     * @param playerUUID 玩家 UUID（僅 PLAYER 類型需要）
     * @return CompletableFuture 完成時表示刪除成功
     */
    CompletableFuture<Void> deleteDigitalMetadata(String digitalName, String type, UUID playerUUID);
    
    // ==================== Transaction Logging Operations ====================
    // 交易日誌操作（用於審計和查詢）
    
    /**
     * 記錄交易到交易日誌
     * @param sourceUUID 發送者 UUID（可為 null，表示系統交易）
     * @param targetUUID 接收者 UUID（可為 null，表示系統交易）
     * @param digitalName Digital 名稱
     * @param amount 交易金額
     * @param taxAmount 稅收金額
     * @param reason 交易原因
     * @param serverId 伺服器 ID
     * @return CompletableFuture 完成時表示記錄成功
     */
    CompletableFuture<Void> logTransaction(UUID sourceUUID, UUID targetUUID, String digitalName,
                                         BigDecimal amount, BigDecimal taxAmount, String reason, String serverId);
    
    /**
     * 獲取玩家交易紀錄
     * @param playerUUID 玩家 UUID
     * @param limit 限制數量
     * @param offset 偏移量
     * @return CompletableFuture 包含交易紀錄列表
     */
    CompletableFuture<java.util.List<TransactionLogEntry>> getPlayerTransactionLog(UUID playerUUID, int limit, int offset);
    
    /**
     * 搜尋交易紀錄
     * @param playerUUID 玩家 UUID（可為 null）
     * @param digitalName Digital 名稱（可為 null）
     * @param startTime 開始時間（ISO 格式字符串，可為 null）
     * @param endTime 結束時間（ISO 格式字符串，可為 null）
     * @param limit 限制數量
     * @param offset 偏移量
     * @return CompletableFuture 包含交易紀錄列表
     */
    CompletableFuture<java.util.List<TransactionLogEntry>> searchTransactionLogs(UUID playerUUID, String digitalName,
                                                                                  String startTime, String endTime,
                                                                                  int limit, int offset);
    
    // ==================== Aggregation Operations ====================
    // 聚合查詢操作（用於統計和分析）
    
    /**
     * 獲取 Digital 總流通量（所有玩家的總和）
     * @param digitalName Digital 名稱
     * @return CompletableFuture 包含總流通量
     */
    CompletableFuture<BigDecimal> getTotalCirculation(String digitalName);
    
    /**
     * 獲取排行榜
     * @param digitalName Digital 名稱
     * @param limit 限制數量
     * @param offset 偏移量
     * @return CompletableFuture 包含排行榜列表
     */
    CompletableFuture<java.util.List<RankingEntry>> getTopPlayers(String digitalName, int limit, int offset);
    
    /**
     * 獲取玩家資產歷史
     * @param playerUUID 玩家 UUID
     * @param days 天數
     * @return CompletableFuture 包含資產歷史數據（按日期分組）
     */
    CompletableFuture<java.util.Map<String, Object>> getPlayerAssetHistory(UUID playerUUID, int days);
    
    // ==================== Health Check ====================
    
    /**
     * 檢查資料庫健康狀態
     * @return CompletableFuture 包含健康狀態（true 表示健康）
     */
    CompletableFuture<Boolean> checkDatabaseHealth();
    
    // ==================== Utility Methods ====================
    
    /**
     * 重新載入插件配置
     * 
     * <p>重新讀取 config.yml，更新資料庫連接、Redis 設定等。
     * 不會清除現有數據，僅更新配置。</p>
     */
    void reloadPlugin();
    
    /**
     * 清除內部緩存
     * 
     * <p>清除記憶體中的緩存數據，強制下次操作從資料庫讀取。
     * 用於調試或數據修復場景。</p>
     */
    void clearCache();
    
    // ==================== Data Transfer Objects ====================
    
    /**
     * Digital 元數據
     */
    interface DigitalMetadata {
        long getId();
        String getDigitalName();
        String getType();
        UUID getPlayerUUID();
        boolean isRemoveOnZero();
        boolean isProtectionMode();
        java.sql.Timestamp getCreatedAt();
        java.sql.Timestamp getUpdatedAt();
    }
    
    /**
     * 交易日誌條目
     */
    interface TransactionLogEntry {
        long getId();
        UUID getSourceUUID();
        UUID getTargetUUID();
        String getDigitalName();
        BigDecimal getAmount();
        BigDecimal getTaxAmount();
        String getReason();
        String getServerId();
        java.sql.Timestamp getTransactionTime();
    }
    
    /**
     * 排行榜條目
     */
    interface RankingEntry {
        UUID getPlayerUUID();
        BigDecimal getValue();
    }
}

