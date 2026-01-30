package noie.linmimeng.noiedigitalsystem.api;

import noie.linmimeng.noiedigitalsystem.model.Digital;
import noie.linmimeng.noiedigitalsystem.model.PlayerDigital;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-LEGACYAPI-000
 * [Semantic] Legacy API bridge (deprecated).
 *
 * <p><b>Deprecated:</b> use {@link NdsRuntime} and related v2 APIs.</p>
 *
 * @deprecated Use {@link NdsRuntime} and related v2 APIs.
 * @since 1.0.0
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public interface NoieDigitalSystemAPI {

    // ==================== Server Digital Operations ====================
    // [Index] NDS-JAVA-LEGACYAPI-010 [Semantic] Server-scope variables (e.g., world events, boss HP).
    // [Constraint] Methods use BigDecimal for precision and CompletableFuture for async execution.
    
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

    // [Index] NDS-JAVA-LEGACYAPI-011 [Deprecated] double overloads (backward compatibility).
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
    // [Index] NDS-JAVA-LEGACYAPI-020 [Semantic] Player-scope resources (most common operations).
    // [Constraint] Methods use BigDecimal for precision and CompletableFuture for async execution.
    
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

    // [Index] NDS-JAVA-LEGACYAPI-021 [Deprecated] double overloads (backward compatibility).
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
    // [Index] NDS-JAVA-LEGACYAPI-030 [Semantic] Global player Digital definitions (system configuration layer).
    // [Behavior] After creation, all players implicitly have the Digital (with initial value).
    
    CompletableFuture<Void> createGlobalPlayerDigital(String digitalName, BigDecimal initialAmount, BigDecimal limit);
    CompletableFuture<Void> removeGlobalPlayerDigital(String digitalName);
    CompletableFuture<Map<String, Digital>> getAllGlobalPlayerDigitals();
    CompletableFuture<Boolean> isGlobalPlayerDigital(String digitalName);
    
    // [Index] NDS-JAVA-LEGACYAPI-031 [Deprecated] double overloads (backward compatibility).
    default CompletableFuture<Void> createGlobalPlayerDigital(String digitalName, double initialAmount, double limit) {
        return createGlobalPlayerDigital(digitalName, BigDecimal.valueOf(initialAmount),
                limit > 0 ? BigDecimal.valueOf(limit) : null);
    }

    // ==================== Digital Metadata Operations ====================
    // [Index] NDS-JAVA-LEGACYAPI-040 [Semantic] Digital metadata management (behavior/config).
    
    CompletableFuture<Void> createDigitalMetadata(String digitalName, String type, UUID playerUUID, 
                                                  boolean removeOnZero, boolean protectionMode);
    
    CompletableFuture<DigitalMetadata> getDigitalMetadata(String digitalName, String type, UUID playerUUID);
    
    CompletableFuture<Void> updateDigitalMetadata(String digitalName, String type, UUID playerUUID,
                                                 Boolean removeOnZero, Boolean protectionMode);
    
    CompletableFuture<Void> deleteDigitalMetadata(String digitalName, String type, UUID playerUUID);
    
    // ==================== Transaction Logging Operations ====================
    // [Index] NDS-JAVA-LEGACYAPI-050 [Semantic] Transaction logging (audit/query support).
    
    CompletableFuture<Void> logTransaction(UUID sourceUUID, UUID targetUUID, String digitalName,
                                         BigDecimal amount, BigDecimal taxAmount, String reason, String serverId);
    
    CompletableFuture<java.util.List<TransactionLogEntry>> getPlayerTransactionLog(UUID playerUUID, int limit, int offset);
    
    CompletableFuture<java.util.List<TransactionLogEntry>> searchTransactionLogs(UUID playerUUID, String digitalName,
                                                                                  String startTime, String endTime,
                                                                                  int limit, int offset);
    
    // ==================== Aggregation Operations ====================
    // [Index] NDS-JAVA-LEGACYAPI-060 [Semantic] Aggregation queries (statistics/analytics).
    
    CompletableFuture<BigDecimal> getTotalCirculation(String digitalName);
    
    CompletableFuture<java.util.List<RankingEntry>> getTopPlayers(String digitalName, int limit, int offset);
    
    CompletableFuture<java.util.Map<String, Object>> getPlayerAssetHistory(UUID playerUUID, int days);
    
    // ==================== Health Check ====================
    
    CompletableFuture<Boolean> checkDatabaseHealth();
    
    // ==================== Utility Methods ====================
    
    void reloadPlugin();
    
    void clearCache();
    
    // ==================== Data Transfer Objects ====================
    
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
    
    interface RankingEntry {
        UUID getPlayerUUID();
        BigDecimal getValue();
    }
}

