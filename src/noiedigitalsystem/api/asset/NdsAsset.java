package noie.linmimeng.noiedigitalsystem.api.asset;

import java.util.Map;

/**
 * NDS Asset - 資產模型
 * 
 * <p>代表系統中的任何資產，包括貨幣、物品、聲望等。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>tags 無上限，用於 AI / UI / Query 的語意入口</li>
 *   <li>必須支持語意化、向量化、分析</li>
 *   <li>Asset 本身不包含數值，數值由 Transaction 管理</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsAsset {
    
    /**
     * 獲取資產 ID
     * 
     * @return 資產 ID
     */
    AssetId assetId();
    
    /**
     * 獲取資產作用域
     * 
     * @return 作用域
     */
    AssetScope scope();
    
    /**
     * 獲取資產標籤
     * 
     * <p>tags 用於：</p>
     * <ul>
     *   <li>AI 語意化查詢</li>
     *   <li>UI 分類顯示</li>
     *   <li>向量化分析</li>
     * </ul>
     * 
     * @return 標籤映射（可為空，但不為 null）
     */
    Map<String, String> tags();
    
    /**
     * 獲取資產元數據
     * 
     * @return 元數據映射（可為空，但不為 null）
     */
    Map<String, String> metadata();
    
    /**
     * 檢查資產是否有效
     * 
     * @return true 如果資產有效
     */
    default boolean isValid() {
        return assetId() != null && scope() != null;
    }
    
    /**
     * 檢查是否包含指定標籤
     * 
     * @param key 標籤鍵
     * @param value 標籤值（可為 null，表示只檢查鍵）
     * @return true 如果包含標籤
     */
    default boolean hasTag(String key, String value) {
        if (key == null) return false;
        String tagValue = tags().get(key);
        if (value == null) {
            return tagValue != null;
        }
        return value.equals(tagValue);
    }
}

