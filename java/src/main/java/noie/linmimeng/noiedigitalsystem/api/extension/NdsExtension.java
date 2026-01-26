package noie.linmimeng.noiedigitalsystem.api.extension;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;

/**
 * NDS Extension - 擴展槽（安全擴展）
 * 
 * <p>允許插件擴展 NDS 功能，但必須遵守安全規則。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>❌ 禁止修改事件</li>
 *   <li>✅ 只能產生新事件</li>
 *   <li>✅ 必須是純函數（無副作用）</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsExtension {
    
    /**
     * 獲取擴展命名空間
     * 
     * <p>用於區分不同插件的擴展。</p>
     * 
     * @return 命名空間（如 "myplugin", "shop"）
     */
    String namespace();
    
    /**
     * 處理事件
     * 
     * <p>當事件發生時，擴展可以處理並產生新事件。</p>
     * 
     * <p><b>重要：</b></p>
     * <ul>
     *   <li>不得修改輸入事件</li>
     *   <li>只能返回新事件或空結果</li>
     *   <li>不得阻塞線程</li>
     * </ul>
     * 
     * @param event 輸入事件（Immutable）
     * @return 處理結果（包含新事件列表，如果沒有則返回空列表）
     */
    NdsResult<List<NdsEvent>> onEvent(NdsEvent event);
    
    /**
     * 獲取擴展優先級
     * 
     * <p>用於決定擴展的執行順序。數字越小，優先級越高。</p>
     * 
     * @return 優先級（默認為 100）
     */
    default int priority() {
        return 100;
    }
    
    /**
     * 檢查擴展是否啟用
     * 
     * @return true 如果啟用
     */
    default boolean isEnabled() {
        return true;
    }
    
    /**
     * 當擴展被註冊時調用
     * 
     * <p>用於初始化擴展的資源，例如：</p>
     * <ul>
     *   <li>連線到自己的 Redis</li>
     *   <li>建立自己的緩存</li>
     *   <li>初始化配置</li>
     * </ul>
     * 
     * <p><b>注意：</b>這是 default 方法，不強制實作。如果擴展不需要初始化，可以不覆蓋此方法。</p>
     */
    default void onEnable() {
        // [Index] NDS-JAVA-EXTENSION-001 [Behavior] Default no-op implementation.
    }
    
    /**
     * 當擴展被卸載或服務器關閉時調用
     * 
     * <p>用於清理擴展的資源，例如：</p>
     * <ul>
     *   <li>關閉 Redis 連接</li>
     *   <li>清理緩存</li>
     *   <li>保存狀態</li>
     * </ul>
     * 
     * <p><b>注意：</b>這是 default 方法，不強制實作。如果擴展不需要清理，可以不覆蓋此方法。</p>
     */
    default void onDisable() {
        // [Index] NDS-JAVA-EXTENSION-002 [Behavior] Default no-op implementation.
    }
}

