package noie.linmimeng.noiedigitalsystem.api;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEventBus;
import noie.linmimeng.noiedigitalsystem.api.projection.NdsQueryService;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentityService;
import noie.linmimeng.noiedigitalsystem.api.platform.NdsPlatform;
import java.util.concurrent.Executor;

/**
 * NDS Runtime - 政府接口
 * 
 * <p>這是 NDS 系統的核心運行時接口，由 nds-core 實作。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>所有方法必須線程安全</li>
 *   <li>所有方法不得阻塞調用線程</li>
 *   <li>不得返回 null（可返回空集合或 Optional）</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsRuntime {
    
    /**
     * 獲取事件總線
     * 
     * @return 事件總線實例（永不為 null）
     */
    NdsEventBus eventBus();
    
    /**
     * 獲取查詢服務
     * 
     * @return 查詢服務實例（永不為 null）
     */
    NdsQueryService query();
    
    /**
     * 獲取身份服務
     * 
     * @return 身份服務實例（永不為 null）
     */
    NdsIdentityService identity();
    
    /**
     * 獲取運行時版本
     * 
     * @return 版本字符串（格式：MAJOR.MINOR.PATCH）
     */
    String version();
    
    /**
     * 檢查運行時健康狀態
     * 
     * @return true 如果運行時健康
     */
    boolean isHealthy();
    
    /**
     * 獲取平台抽象接口
     * 
     * @return 平台接口（永不為 null）
     * @since 2.0.0
     */
    NdsPlatform platform();
    
    /**
     * 獲取預設執行器（推薦使用）
     * 
     * <p><b>⚠️ 重要：異步回調線程責任</b></p>
     * <p>所有 API 方法返回的 CompletableFuture 的回調（如 thenAccept, thenApply）
     * 可能在異步線程執行。在不同環境中需要不同的執行器：</p>
     * <ul>
     *   <li>Minecraft 環境：必須使用主線程執行器（用於操作線程不安全的 Bukkit API）</li>
     *   <li>Spring Boot 環境：建議使用 Spring 管理的執行器</li>
     *   <li>通用 Java 環境：可使用預設執行器</li>
     * </ul>
     * 
     * <p><b>使用範例：</b></p>
     * <pre>{@code
     * runtime.query().queryBalance(assetId, identity)
     *     .thenAcceptAsync(result -> {
     *         if (result.isSuccess()) {
     *             // 在 Minecraft 環境中，此處必須在主線程
     *             // 在 Spring Boot 環境中，此處在 Spring 管理的線程
     *             System.out.println("Balance: " + result.data());
     *         }
     *     }, runtime.defaultExecutor());
     * }</pre>
     * 
     * @return 預設執行器（永不為 null）
     * @since 2.0.0
     */
    default Executor defaultExecutor() {
        return platform().defaultExecutor();
    }
    
    /**
     * 獲取主線程執行器（向後兼容方法）
     * 
     * <p>此方法已棄用，建議使用 {@link #defaultExecutor()} 以獲得更好的平台抽象。</p>
     * 
     * @return 主線程執行器（永不為 null）
     * @deprecated 建議使用 {@link #defaultExecutor()} 以獲得更好的平台抽象
     */
    @Deprecated(since = "2.0.0", forRemoval = false)
    default Executor mainThreadExecutor() {
        return defaultExecutor();
    }
}

