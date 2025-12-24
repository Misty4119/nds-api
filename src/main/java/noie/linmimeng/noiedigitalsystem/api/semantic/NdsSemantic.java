package noie.linmimeng.noiedigitalsystem.api.semantic;

import java.util.Map;
import java.util.Optional;

/**
 * NDS Semantic - 語義接口
 * 
 * <p>用於表示語義化的數據，支持 AI 理解和分析。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>必須支持上下文信息</li>
 *   <li>必須支持可選的向量嵌入</li>
 *   <li>必須 Immutable</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsSemantic {
    
    /**
     * 獲取上下文信息
     * 
     * <p>包含語義化的上下文數據，用於 AI 理解。</p>
     * 
     * @return 上下文映射（可為空，但不為 null）
     */
    Map<String, String> context();
    
    /**
     * 獲取向量嵌入（可選）
     * 
     * <p>如果存在，則表示已經向量化的數據。</p>
     * 
     * @return 向量嵌入，如果不存在則返回 empty
     */
    Optional<NdsTensor> embedding();
    
    /**
     * 獲取上下文值
     * 
     * @param key 鍵
     * @return 值，如果不存在則返回 null
     */
    default String getContext(String key) {
        return context().get(key);
    }
    
    /**
     * 檢查是否包含上下文鍵
     * 
     * @param key 鍵
     * @return true 如果包含
     */
    default boolean hasContext(String key) {
        return context().containsKey(key);
    }
    
    /**
     * 創建語義對象（無嵌入）
     * 
     * @param context 上下文映射
     * @return 語義對象
     */
    static NdsSemantic of(Map<String, String> context) {
        return new NdsSemanticImpl(context, Optional.empty());
    }
    
    /**
     * 創建語義對象（帶嵌入）
     * 
     * @param context 上下文映射
     * @param embedding 向量嵌入
     * @return 語義對象
     */
    static NdsSemantic of(Map<String, String> context, NdsTensor embedding) {
        return new NdsSemanticImpl(context, Optional.of(embedding));
    }
}

