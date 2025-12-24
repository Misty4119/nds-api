package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * Event Type - 事件類型
 * 
 * <p>定義系統支持的事件類型。</p>
 * 
 * @since 2.0.0
 */
public enum EventType {
    /**
     * 交易事件
     * 用於資產轉移（見 NdsTransaction）
     */
    TRANSACTION,
    
    /**
     * 資產創建事件
     * 用於創建新資產
     */
    ASSET_CREATED,
    
    /**
     * 資產更新事件
     * 用於更新資產元數據
     */
    ASSET_UPDATED,
    
    /**
     * 資產刪除事件
     * 用於刪除資產
     */
    ASSET_DELETED,
    
    /**
     * 身份創建事件
     * 用於創建新身份
     */
    IDENTITY_CREATED,
    
    /**
     * 身份更新事件
     * 用於更新身份元數據
     */
    IDENTITY_UPDATED,
    
    /**
     * 系統事件
     * 用於系統級操作
     */
    SYSTEM,
    
    /**
     * 自定義事件
     * 用於擴展
     */
    CUSTOM;
    
    /**
     * 從字符串解析事件類型
     * 
     * @param str 字符串
     * @return 事件類型，如果無法解析則返回 CUSTOM
     */
    public static EventType fromString(String str) {
        if (str == null) return CUSTOM;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CUSTOM;
        }
    }
}

