package noie.linmimeng.noiedigitalsystem.api.platform;

import java.util.concurrent.Executor;

/**
 * NDS Platform - 平台抽象接口
 * 
 * <p>此接口用於抽象不同運行環境的平台特性：</p>
 * <ul>
 *   <li>Minecraft 環境：提供主線程執行器（用於操作線程不安全的平台特定 API）</li>
 *   <li>Spring Boot 環境：提供 Spring 管理的執行器</li>
 *   <li>通用 Java 環境：提供預設執行器（ForkJoinPool）</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsPlatform {
    
    /**
     * 獲取預設執行器
     * 
     * <p>此執行器用於執行異步回調。在不同環境中可能有不同實作：</p>
     * <ul>
     *   <li>Minecraft 環境：返回主線程執行器（用於操作線程不安全的平台特定 API）</li>
     *   <li>Spring Boot 環境：返回 Spring 管理的執行器</li>
     *   <li>通用 Java 環境：返回預設執行器（ForkJoinPool.commonPool()）</li>
     * </ul>
     * 
     * @return 預設執行器（永不為 null）
     */
    Executor defaultExecutor();
    
    /**
     * 獲取平台名稱
     * 
     * @return 平台名稱（如 "Minecraft", "Spring Boot", "Standard Java"）
     */
    String platformName();
    
    /**
     * 檢查是否為 Minecraft 環境
     * 
     * @return true 如果是 Minecraft 環境
     */
    default boolean isMinecraft() {
        return false;
    }
}

