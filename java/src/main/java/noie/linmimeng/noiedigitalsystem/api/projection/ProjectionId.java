package noie.linmimeng.noiedigitalsystem.api.projection;

/**
 * Projection ID - 投影標識符
 * 
 * <p>投影的唯一標識符。</p>
 * 
 * @since 2.0.0
 */
public interface ProjectionId {
    
    /**
     * 獲取 ID 字符串
     * 
     * @return ID 字符串（格式：namespace:name，如 "balance:player:coins"）
     */
    String value();
    
    /**
     * 獲取命名空間
     * 
     * @return 命名空間（如 "balance", "asset"）
     */
    String namespace();
    
    /**
     * 獲取名稱
     * 
     * @return 名稱（如 "player:coins"）
     */
    String name();
    
    /**
     * 創建 ProjectionId
     * 
     * @param namespace 命名空間
     * @param name 名稱
     * @return ProjectionId 實例
     */
    static ProjectionId of(String namespace, String name) {
        return new ProjectionIdImpl(namespace, name);
    }
    
    /**
     * 從字符串解析 ProjectionId
     * 
     * @param value ID 字符串
     * @return ProjectionId 實例
     * @throws IllegalArgumentException 如果格式無效
     */
    static ProjectionId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ProjectionId cannot be null or empty");
        }
        int colonIndex = value.indexOf(':');
        if (colonIndex <= 0 || colonIndex >= value.length() - 1) {
            throw new IllegalArgumentException("Invalid ProjectionId format: " + value);
        }
        String namespace = value.substring(0, colonIndex);
        String name = value.substring(colonIndex + 1);
        return of(namespace, name);
    }
}

