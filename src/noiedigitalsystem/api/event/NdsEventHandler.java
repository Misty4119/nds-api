package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * NDS Event Handler - 事件處理器
 * 
 * <p>用於處理訂閱的事件。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>處理器不得修改事件</li>
 *   <li>處理器應該快速執行，避免阻塞</li>
 *   <li>處理器中的異常不會影響事件發布</li>
 * </ul>
 * 
 * @since 2.0.0
 */
@FunctionalInterface
public interface NdsEventHandler {
    
    /**
     * 處理事件
     * 
     * @param event 事件對象（Immutable）
     */
    void handle(NdsEvent event);
}

