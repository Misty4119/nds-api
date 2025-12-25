package noie.linmimeng.noiedigitalsystem.api.identity;

import java.util.Map;

/**
 * NDS Identity - 身份岩層（未來 DID 基礎）
 * 
 * <p>代表系統中的任何身份實體，包括玩家、系統、AI、外部服務等。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>必須 Immutable（不可變）</li>
 *   <li>id() 必須全局唯一</li>
 *   <li>支持未來 DID（去中心化身份）擴展</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsIdentity {
    
    /**
     * 獲取身份 ID
     * 
     * @return 全局唯一的身份 ID（格式由類型決定）
     */
    String id();
    
    /**
     * 獲取身份類型
     * 
     * @return 身份類型
     */
    IdentityType type();
    
    /**
     * 獲取身份元數據
     * 
     * @return 元數據映射（可為空，但不為 null）
     */
    Map<String, String> metadata();
    
    /**
     * 檢查身份是否有效
     * 
     * @return true 如果身份有效
     */
    default boolean isValid() {
        return id() != null && !id().isEmpty() && type() != null;
    }
    
    /**
     * 創建身份副本（用於 Immutable 模式）
     * 
     * @param metadata 新的元數據（可為 null）
     * @return 新的身份實例
     */
    NdsIdentity withMetadata(Map<String, String> metadata);
    
    /**
     * 靜態工廠：從字符串構建 (格式 type:id 或 純id)
     * 
     * <p>這是 Phase 4 默認方法能運作的關鍵。</p>
     * <p>NdsIdentity 應該是一個輕量級的 Value Object（值對象），不需要查詢資料庫就能創建（只要有 ID 和 Type 即可）。</p>
     * 
     * <p><b>使用場景：</b></p>
     * <ul>
     *   <li>當一個身份登入事件發生（同步），需要發出一個 NdsEvent</li>
     *   <li>手上有 ID 字符串，需要快速創建 NdsIdentity 對象</li>
     *   <li>不需要異步查庫，直接創建輕量級對象</li>
     *   <li>例如：在 Minecraft 環境中，當玩家登入時，使用玩家的 UUID 創建 NdsIdentity</li>
     * </ul>
     * 
     * <p><b>格式說明：</b></p>
     * <ul>
     *   <li>格式 1：type:id（如 "PLAYER:550e8400-e29b-41d4-a716-446655440000"）</li>
     *   <li>格式 2：純 id（默認假定為 PLAYER 類型）</li>
     * </ul>
     * 
     * @param rawId 原始 ID 字符串
     * @return NdsIdentity 實例
     * @throws IllegalArgumentException 如果格式無效
     */
    static NdsIdentity fromString(String rawId) {
        if (rawId == null || rawId.isEmpty()) {
            throw new IllegalArgumentException("Identity ID cannot be null or empty");
        }
        if (rawId.contains(":")) {
            String[] parts = rawId.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid identity format: " + rawId);
            }
            IdentityType type = IdentityType.fromString(parts[0]);
            return of(parts[1], type);
        }
        // 默認假定為 PLAYER 或根据上下文判断，這裡建議強制要求格式
        return of(rawId, IdentityType.PLAYER);
    }
    
    /**
     * 靜態工廠：構建指定類型
     * 
     * <p>輕量級創建，不需要查庫。</p>
     * 
     * @param id 身份 ID
     * @param type 身份類型
     * @return NdsIdentity 實例
     */
    static NdsIdentity of(String id, IdentityType type) {
        return new NdsIdentityImpl(id, type, Map.of()); // 輕量級創建
    }
}

