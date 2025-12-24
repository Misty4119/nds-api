package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Event Bus - 事件總線接口（完整定義）
 * 
 * @since 2.0.0
 */
public interface NdsEventBus {
    
    /**
     * 發布事件
     * 
     * <p><b>⚠️ 重要：Publish Success = Persisted（持久化成功）</b></p>
     * <p>返回的 Future 完成代表事件已經<b>成功持久化到事件存儲（Event Store）</b>。</p>
     * 
     * <p><b>語義澄清：</b></p>
     * <ul>
     *   <li>✅ Future 完成 = 事件已成功持久化（Persisted）</li>
     *   <li>❌ Future 完成 ≠ 僅僅放入內存隊列</li>
     *   <li>❌ Future 完成 ≠ 所有訂閱者都處理完畢</li>
     * </ul>
     * 
     * <p>訂閱者（Extensions）可能在持久化過程中或之後異步處理事件，但此處的結果保證數據安全。</p>
     * 
     * <p>鑑於憲法原則 <b>"Event Is The Source Of Truth"</b>，持久化是發布成功的唯一標準。</p>
     * 
     * @param event 要發布的事件（必須 Immutable）
     * @return CompletableFuture 包含發布結果，完成時表示事件已成功持久化
     */
    CompletableFuture<NdsResult<Void>> publish(NdsEvent event);
    
    /**
     * 訂閱事件
     * 
     * @param eventType 事件類型
     * @param handler 事件處理器
     * @return 訂閱 ID（用於取消訂閱）
     */
    String subscribe(EventType eventType, NdsEventHandler handler);
    
    /**
     * 訂閱所有事件
     * 
     * @param handler 事件處理器
     * @return 訂閱 ID
     */
    String subscribeAll(NdsEventHandler handler);
    
    /**
     * 取消訂閱
     * 
     * @param subscriptionId 訂閱 ID
     */
    void unsubscribe(String subscriptionId);
    
    /**
     * 查詢歷史事件
     * 
     * @param eventType 事件類型（可為 null，表示所有類型）
     * @param startTime 開始時間（可為 null）
     * @param endTime 結束時間（可為 null）
     * @param limit 限制數量
     * @param offset 偏移量
     * @return CompletableFuture 包含事件列表
     */
    CompletableFuture<NdsResult<List<NdsEvent>>> queryHistory(
        EventType eventType,
        Instant startTime,
        Instant endTime,
        int limit,
        int offset
    );
}

