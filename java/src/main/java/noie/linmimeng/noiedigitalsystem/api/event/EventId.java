package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * Event ID - 事件標識符
 * 
 * <p>事件的全局唯一標識符。</p>
 * 
 * @since 2.0.0
 */
public interface EventId {
    
    /**
     * 獲取 ID 字符串
     * 
     * @return ID 字符串（格式：UUID 或時間戳+隨機數）
     */
    String value();
    
    /**
     * 創建新的 EventId
     * 
     * @return 新的 EventId 實例
     */
    static EventId generate() {
        return new EventIdImpl(java.util.UUID.randomUUID().toString());
    }
    
    /**
     * 從字符串創建 EventId
     * 
     * @param value ID 字符串
     * @return EventId 實例
     */
    static EventId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("EventId cannot be null or empty");
        }
        return new EventIdImpl(value);
    }
}

