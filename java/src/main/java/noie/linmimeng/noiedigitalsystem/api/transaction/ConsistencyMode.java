package noie.linmimeng.noiedigitalsystem.api.transaction;

/**
 * Consistency Mode - 一致性模式
 * 
 * <p>定義交易的一致性要求。</p>
 * 
 * @since 2.0.0
 */
public enum ConsistencyMode {
    /**
     * 強一致性
     * 交易必須立即生效，保證數據一致性
     * 適用於：關鍵經濟操作（支付、轉賬）
     */
    STRONG,
    
    /**
     * 最終一致性
     * 交易可以延遲生效，最終保證一致性
     * 適用於：非關鍵操作（統計、日誌）
     */
    EVENTUAL,
    
    /**
     * 樂觀一致性
     * 交易基於樂觀鎖，可能失敗
     * 適用於：高並發場景
     */
    OPTIMISTIC;
    
    /**
     * 從字符串解析一致性模式
     * 
     * @param str 字符串
     * @return 一致性模式，如果無法解析則返回 STRONG
     */
    public static ConsistencyMode fromString(String str) {
        if (str == null) return STRONG;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STRONG;
        }
    }
}

