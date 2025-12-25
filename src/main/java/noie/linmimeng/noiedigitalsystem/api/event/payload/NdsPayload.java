package noie.linmimeng.noiedigitalsystem.api.event.payload;

import java.util.Map;
import java.util.Set;

/**
 * NDS Payload - 序列化契約（防禦層）
 * 
 * <p>事件負載必須只包含可序列化的基本類型。</p>
 * 
 * <p><b>死刑條款：</b></p>
 * <ul>
 *   <li>嚴禁 Complex Object（平台特定對象，如 Minecraft 環境中的 Entity、Location、ItemStack）</li>
 *   <li>嚴禁平台特定類型（如 Bukkit/Paper、Spring Bean 等）</li>
 *   <li>嚴禁不可序列化的對象</li>
 *   <li>Core 必須拒絕違規事件</li>
 * </ul>
 * 
 * <p><b>允許的類型：</b></p>
 * <ul>
 *   <li>String, Integer, Long, Double, Boolean</li>
 *   <li>BigDecimal, BigInteger</li>
 *   <li>List&lt;String&gt;, Map&lt;String, Object&gt;</li>
 *   <li>null</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsPayload {
    
    /**
     * 獲取字符串值
     * 
     * @param key 鍵
     * @return 字符串值，如果不存在則返回 null
     */
    String getString(String key);
    
    /**
     * 獲取整數值
     * 
     * @param key 鍵
     * @return 整數值，如果不存在則返回 null
     */
    Integer getInt(String key);
    
    /**
     * 獲取長整數值
     * 
     * @param key 鍵
     * @return 長整數值，如果不存在則返回 null
     */
    Long getLong(String key);
    
    /**
     * 獲取雙精度浮點數值
     * 
     * @param key 鍵
     * @return 雙精度浮點數值，如果不存在則返回 null
     */
    Double getDouble(String key);
    
    /**
     * 獲取布爾值
     * 
     * @param key 鍵
     * @return 布爾值，如果不存在則返回 null
     */
    Boolean getBoolean(String key);
    
    /**
     * 獲取 BigDecimal 值
     * 
     * @param key 鍵
     * @return BigDecimal 值，如果不存在則返回 null
     */
    java.math.BigDecimal getBigDecimal(String key);
    
    /**
     * 獲取列表值
     * 
     * @param key 鍵
     * @return 字符串列表，如果不存在則返回空列表
     */
    java.util.List<String> getList(String key);
    
    /**
     * 獲取映射值
     * 
     * @param key 鍵
     * @return 映射，如果不存在則返回空映射
     */
    Map<String, Object> getMap(String key);
    
    /**
     * 轉換為原始映射
     * 
     * @return 原始映射（用於序列化）
     */
    Map<String, Object> toRawMap();
    
    /**
     * 檢查是否包含鍵
     * 
     * @param key 鍵
     * @return true 如果包含鍵
     */
    boolean containsKey(String key);
    
    /**
     * 獲取所有鍵
     * 
     * @return 鍵集合
     */
    Set<String> keys();
    
    /**
     * 檢查負載是否為空
     * 
     * @return true 如果為空
     */
    boolean isEmpty();
}

