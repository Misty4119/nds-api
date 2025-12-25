package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.payload.NdsPayload;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * NDS Event - 事件岩層（唯一真實）
 * 
 * <p>所有系統狀態變更都必須通過事件來表達。事件是不可變的歷史記錄。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>Event 必須 Immutable（不可變）</li>
 *   <li>禁止攜帶平台特定類型或不可序列化對象（如 Minecraft 環境中的 Entity、Location、ItemStack 等）</li>
 *   <li>必須可序列化</li>
 *   <li>必須支持 Replay（重放）</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsEvent {
    
    /**
     * 獲取事件 ID
     * 
     * @return 全局唯一的事件 ID
     */
    EventId id();
    
    /**
     * 獲取事件發生時間
     * 
     * @return 事件發生時間（UTC）
     */
    Instant occurredAt();
    
    /**
     * 獲取事件發起者
     * 
     * @return 身份對象（可為系統身份）
     */
    NdsIdentity actor();
    
    /**
     * 獲取事件類型
     * 
     * @return 事件類型
     */
    EventType type();
    
    /**
     * 獲取事件負載
     * 
     * @return 事件負載（包含事件數據）
     */
    NdsPayload payload();
    
    /**
     * 獲取事件模式版本
     * 
     * <p>用於向後兼容和遷移。</p>
     * 
     * @return 模式版本（默認為 1）
     */
    default int schemaVersion() {
        return 1;
    }
    
    /**
     * 獲取事件元數據
     * 
     * @return 元數據映射（可為空，但不為 null）
     */
    default Map<String, String> metadata() {
        return Collections.emptyMap();
    }
    
    /**
     * 檢查事件是否有效
     * 
     * @return true 如果事件有效
     */
    default boolean isValid() {
        return id() != null 
            && occurredAt() != null 
            && actor() != null 
            && actor().isValid()
            && type() != null 
            && payload() != null;
    }
}

