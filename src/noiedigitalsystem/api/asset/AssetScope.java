package noie.linmimeng.noiedigitalsystem.api.asset;

/**
 * Asset Scope - 資產作用域
 * 
 * <p>定義資產的作用範圍。</p>
 * 
 * @since 2.0.0
 */
public enum AssetScope {
    /**
     * 玩家作用域
     * 資產屬於特定玩家（如 "coins", "gold"）
     */
    PLAYER,
    
    /**
     * 服務器作用域
     * 資產屬於整個服務器（如 "world_boss_hp", "server_balance"）
     */
    SERVER,
    
    /**
     * 全局作用域
     * 資產屬於整個系統（如 "total_circulation"）
     */
    GLOBAL,
    
    /**
     * 未知作用域（用於向後兼容）
     */
    UNKNOWN;
    
    /**
     * 從字符串解析作用域
     * 
     * @param str 字符串
     * @return 作用域，如果無法解析則返回 UNKNOWN
     */
    public static AssetScope fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

