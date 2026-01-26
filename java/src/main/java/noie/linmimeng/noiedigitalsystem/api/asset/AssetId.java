package noie.linmimeng.noiedigitalsystem.api.asset;

/**
 * Asset ID - 資產標識符
 * 
 * <p>資產的唯一標識符，必須全局唯一。</p>
 * 
 * @since 2.0.0
 */
public interface AssetId {
    
    /**
     * 獲取資產名稱
     * 
     * @return 資產名稱（如 "coins", "gold", "world_boss_hp"）
     */
    String name();
    
    /**
     * 獲取完整 ID 字符串
     * 
     * @return 完整 ID（格式：scope:name，如 "player:coins", "server:world_boss_hp"）
     */
    String fullId();
    
    /**
     * 獲取作用域
     * 
     * @return 作用域
     */
    AssetScope scope();
    
    /**
     * 從字符串解析 AssetId
     * 
     * @param fullId 完整 ID 字符串
     * @return AssetId 實例
     * @throws IllegalArgumentException 如果格式無效
     */
    static AssetId fromString(String fullId) {
        if (fullId == null || fullId.isEmpty()) {
            throw new IllegalArgumentException("AssetId cannot be null or empty");
        }
        int colonIndex = fullId.indexOf(':');
        if (colonIndex <= 0 || colonIndex >= fullId.length() - 1) {
            throw new IllegalArgumentException("Invalid AssetId format: " + fullId);
        }
        String scopeStr = fullId.substring(0, colonIndex);
        String name = fullId.substring(colonIndex + 1);
        AssetScope scope = AssetScope.fromString(scopeStr);
        return of(scope, name);
    }
    
    /**
     * 創建 AssetId
     * 
     * @param scope 作用域
     * @param name 名稱
     * @return AssetId 實例
     */
    static AssetId of(AssetScope scope, String name) {
        return new AssetIdImpl(scope, name);
    }
}

