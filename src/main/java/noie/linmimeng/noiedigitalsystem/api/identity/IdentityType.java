package noie.linmimeng.noiedigitalsystem.api.identity;

/**
 * 身份類型
 * 
 * @since 2.0.0
 */
public enum IdentityType {
    /**
     * 玩家身份
     * ID 格式：UUID（如 "550e8400-e29b-41d4-a716-446655440000"）
     */
    PLAYER,
    
    /**
     * 系統身份
     * ID 格式：系統定義的字符串（如 "system:admin", "system:event"）
     */
    SYSTEM,
    
    /**
     * AI 身份
     * ID 格式：AI 服務定義的字符串（如 "ai:gpt-4", "ai:claude"）
     */
    AI,
    
    /**
     * 外部服務身份
     * ID 格式：外部服務定義的字符串（如 "external:payment:stripe"）
     */
    EXTERNAL,
    
    /**
     * 未知類型（用於向後兼容）
     */
    UNKNOWN;
    
    /**
     * 從字符串解析身份類型
     * 
     * @param str 字符串
     * @return 身份類型，如果無法解析則返回 UNKNOWN
     */
    public static IdentityType fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

