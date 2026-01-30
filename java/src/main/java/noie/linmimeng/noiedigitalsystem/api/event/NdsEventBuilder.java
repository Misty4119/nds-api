package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.payload.NdsPayload;
import java.time.Instant;
import java.util.Map;

/**
 * NDS Event Builder - 事件構建器
 * 
 * <p>用於構建不可變的事件對象。</p>
 * 
 * @since 2.0.0
 */
public interface NdsEventBuilder {
    
    /**
     * 設置事件 ID
     * 
     * @param id 事件 ID（如果為 null，則自動生成）
     * @return 構建器實例
     */
    NdsEventBuilder id(EventId id);
    
    /**
     * 設置事件發生時間
     * 
     * @param occurredAt 發生時間（如果為 null，則使用當前時間）
     * @return 構建器實例
     */
    NdsEventBuilder occurredAt(Instant occurredAt);
    
    /**
     * 設置事件發起者
     * 
     * @param actor 身份對象（必須）
     * @return 構建器實例
     */
    NdsEventBuilder actor(NdsIdentity actor);
    
    /**
     * 設置事件類型
     * 
     * @param type 事件類型（必須）
     * @return 構建器實例
     */
    NdsEventBuilder type(EventType type);
    
    /**
     * 設置事件負載
     * 
     * @param payload 負載對象（必須）
     * @return 構建器實例
     */
    NdsEventBuilder payload(NdsPayload payload);
    
    /**
     * 設置模式版本
     * 
     * @param schemaVersion 模式版本（默認為 1）
     * @return 構建器實例
     */
    NdsEventBuilder schemaVersion(int schemaVersion);
    
    /**
     * 設置元數據
     * 
     * @param metadata 元數據映射（可為 null）
     * @return 構建器實例
     */
    NdsEventBuilder metadata(Map<String, String> metadata);
    
    /**
     * 構建事件
     * 
     * <p><b>⚠️ 重要：Payload 運行時類型檢查</b></p>
     * <p>在 build() 階段，會對 Payload 內容進行 <b>運行時類型檢查 (Runtime Type Check)</b>。
     * 必須遍歷所有 Value，檢查是否為允許的類型：</p>
     * <ul>
     *   <li>String, Integer, Long, Double, Boolean</li>
     *   <li>BigDecimal, BigInteger</li>
     *   <li>List&lt;String&gt;, Map&lt;String, Object&gt;（嵌套 Map 也會遞歸檢查）</li>
     *   <li>null</li>
     * </ul>
     * 
     * <p>如果發現違規類型（如平台特定對象、不可序列化對象等），
     * <b>當場拋出 IllegalArgumentException</b>，不要等到序列化時才報錯。</p>
     * <p>例如：在 Minecraft 環境中，Entity、Location、ItemStack 等 Bukkit 對象是禁止的。</p>
     * 
     * <p><b>⚠️ 歷史事件重放注意事項</b></p>
     * <p>當從資料庫加載歷史事件進行重放時，<b>必須使用原事件的 ID</b>，不能重新生成。
     * 使用 {@link #id(EventId)} 方法明確指定原 ID。</p>
     * 
     * <p><b>範例：</b></p>
     * <pre>{@code
     * // [Index] NDS-JAVA-EVENTBUILDER-EX-001 [Behavior] New event: auto-generate ID.
     * NdsEvent event = NdsEventBuilder.create()
     *     .actor(identity)
     *     .type(EventType.TRANSACTION)
     *     .payload(payload)
     *     .build(); // [Index] NDS-JAVA-EVENTBUILDER-EX-002 [Behavior] ID is auto-generated.
     * 
     * // [Index] NDS-JAVA-EVENTBUILDER-EX-003 [Constraint] Replay: preserve original ID/time.
     * NdsEvent replayedEvent = NdsEventBuilder.create()
     *     .id(originalEvent.id()) // [Index] NDS-JAVA-EVENTBUILDER-EX-004 [Constraint] Use original ID.
     *     .occurredAt(originalEvent.occurredAt()) // [Index] NDS-JAVA-EVENTBUILDER-EX-005 [Constraint] Use original timestamp.
     *     .actor(originalEvent.actor())
     *     .type(originalEvent.type())
     *     .payload(originalEvent.payload())
     *     .build();
     * }</pre>
     * 
     * @return 不可變的事件對象
     * @throws IllegalStateException 如果必填字段缺失
     * @throws IllegalArgumentException 如果 Payload 包含違規類型
     */
    NdsEvent build();
    
    /**
     * 創建新的事件構建器
     * 
     * @return 新的構建器實例
     */
    static NdsEventBuilder create() {
        // [Index] NDS-JAVA-EVENTBUILDER-900 [Trace] Implementation is provided by nds-core.
        throw new UnsupportedOperationException("NdsEventBuilder implementation must be provided by nds-core");
    }
}

