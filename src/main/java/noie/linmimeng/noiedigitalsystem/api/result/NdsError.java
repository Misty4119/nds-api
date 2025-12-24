package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Map;

/**
 * NDS Error - 錯誤對象
 * 
 * <p>用於表達錯誤信息，支持結構化錯誤數據。</p>
 * 
 * @since 2.0.0
 */
public interface NdsError {
    
    /**
     * 獲取錯誤代碼
     * 
     * @return 錯誤代碼（如 "INSUFFICIENT_BALANCE", "ASSET_NOT_FOUND"）
     */
    String code();
    
    /**
     * 獲取錯誤消息
     * 
     * @return 錯誤消息（人類可讀）
     */
    String message();
    
    /**
     * 獲取錯誤詳情
     * 
     * @return 錯誤詳情映射（可為空，但不為 null）
     */
    Map<String, Object> details();
    
    /**
     * 獲取原始異常（如果有的話）
     * 
     * @return 原始異常，如果不存在則返回 null
     */
    Throwable cause();
    
    /**
     * 創建錯誤對象
     * 
     * @param code 錯誤代碼
     * @param message 錯誤消息
     * @return 錯誤對象
     */
    static NdsError of(String code, String message) {
        return new NdsErrorImpl(code, message, Map.of(), null);
    }
    
    /**
     * 創建錯誤對象（帶詳情）
     * 
     * @param code 錯誤代碼
     * @param message 錯誤消息
     * @param details 錯誤詳情
     * @return 錯誤對象
     */
    static NdsError of(String code, String message, Map<String, Object> details) {
        return new NdsErrorImpl(code, message, details, null);
    }
    
    /**
     * 創建錯誤對象（帶異常）
     * 
     * @param code 錯誤代碼
     * @param message 錯誤消息
     * @param cause 原始異常
     * @return 錯誤對象
     */
    static NdsError of(String code, String message, Throwable cause) {
        return new NdsErrorImpl(code, message, Map.of(), cause);
    }
}

