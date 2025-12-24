package noie.linmimeng.noiedigitalsystem.api.context;

import java.util.Map;

/**
 * NDS Context - 上下文和追蹤
 * 
 * <p>用於追蹤請求鏈路和傳遞上下文信息。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>Context 必須 Immutable</li>
 *   <li>支持分散式追蹤</li>
 *   <li>支持上下文傳播</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsContext {
    
    /**
     * 獲取追蹤 ID
     * 
     * <p>用於分散式追蹤，全局唯一。</p>
     * 
     * @return 追蹤 ID（格式：UUID）
     */
    String traceId();
    
    /**
     * 獲取關聯 ID
     * 
     * <p>用於關聯相關操作，可以與 traceId 相同或不同。</p>
     * 
     * @return 關聯 ID
     */
    String correlationId();
    
    /**
     * 獲取元數據
     * 
     * @return 元數據映射（可為空，但不為 null）
     */
    Map<String, String> meta();
    
    /**
     * 獲取元數據值
     * 
     * @param key 鍵
     * @return 值，如果不存在則返回 null
     */
    default String getMeta(String key) {
        return meta().get(key);
    }
    
    /**
     * 創建新的上下文（帶額外元數據）
     * 
     * @param additionalMeta 額外元數據
     * @return 新的上下文實例
     */
    NdsContext withMeta(Map<String, String> additionalMeta);
    
    /**
     * 創建新的上下文（帶單個元數據）
     * 
     * @param key 鍵
     * @param value 值
     * @return 新的上下文實例
     */
    NdsContext withMeta(String key, String value);
    
    /**
     * 創建新的上下文
     * 
     * @param traceId 追蹤 ID（如果為 null，則自動生成）
     * @param correlationId 關聯 ID（如果為 null，則使用 traceId）
     * @return 新的上下文實例
     */
    static NdsContext create(String traceId, String correlationId) {
        return new NdsContextImpl(
            traceId != null ? traceId : java.util.UUID.randomUUID().toString(),
            correlationId != null ? correlationId : traceId,
            Map.of()
        );
    }
    
    /**
     * 創建新的上下文（自動生成 ID）
     * 
     * @return 新的上下文實例
     */
    static NdsContext create() {
        String traceId = java.util.UUID.randomUUID().toString();
        return create(traceId, traceId);
    }
}

