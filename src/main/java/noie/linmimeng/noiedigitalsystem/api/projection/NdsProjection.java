package noie.linmimeng.noiedigitalsystem.api.projection;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import java.util.List;

/**
 * NDS Projection - 投影接口
 * 
 * <p>用於從事件列表投影出狀態。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>Projection 必須是純函數（無副作用）</li>
 *   <li>Projection 必須支持重放</li>
 *   <li>Projection 結果必須 Immutable</li>
 * </ul>
 * 
 * @param <T> 投影結果類型
 * @since 2.0.0
 */
public interface NdsProjection<T> {
    
    /**
     * 獲取投影 ID
     * 
     * @return 投影 ID（全局唯一）
     */
    ProjectionId id();
    
    /**
     * 應用事件列表，投影出狀態
     * 
     * <p>這是純函數，不應該有副作用。</p>
     * 
     * @param events 事件列表（按時間順序）
     * @return 投影結果
     */
    T apply(List<NdsEvent> events);
    
    /**
     * 應用單個事件，更新投影狀態
     * 
     * <p>用於增量更新。</p>
     * 
     * @param currentState 當前狀態（可為 null，表示初始狀態）
     * @param event 新事件
     * @return 更新後的狀態
     */
    default T apply(T currentState, NdsEvent event) {
        // 默認實作：重新計算所有事件
        // 子類可以覆蓋以實現增量更新
        List<NdsEvent> allEvents = currentState != null 
            ? List.of() // 需要從存儲中獲取所有事件
            : List.of(event);
        return apply(allEvents);
    }
    
    /**
     * 獲取投影名稱
     * 
     * @return 投影名稱（用於日誌和調試）
     */
    default String name() {
        return id().value();
    }
}

