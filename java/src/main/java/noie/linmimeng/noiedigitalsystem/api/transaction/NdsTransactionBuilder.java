package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * [Index] NDS-JAVA-TRANSACTIONBUILDER-000
 * [Semantic] Builder for immutable {@link NdsTransaction} instances.
 *
 * <p><b>Important: avoid the polymorphism trap</b></p>
 * <p>{@link NdsTransaction} is a specialized event type. Do not use
 * {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEventBuilder} for transactions, because
 * it produces {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEvent} and cannot be safely cast to
 * {@link NdsTransaction}. Use {@link NdsTransactionBuilder} instead.</p>
 * 
 * @since 2.0.0
 */
public interface NdsTransactionBuilder {
    
    /**
     * 設置交易 ID
     * 
     * @param id 交易 ID（如果為 null，則自動生成）
     * @return 構建器實例
     */
    NdsTransactionBuilder id(EventId id);
    
    /**
     * 設置交易發生時間
     * 
     * @param occurredAt 發生時間（如果為 null，則使用當前時間）
     * @return 構建器實例
     */
    NdsTransactionBuilder occurredAt(Instant occurredAt);
    
    /**
     * 設置交易發起者
     * 
     * @param actor 身份對象（必須）
     * @return 構建器實例
     */
    NdsTransactionBuilder actor(NdsIdentity actor);
    
    /**
     * 設置資產 ID
     * 
     * @param asset 資產 ID（必須）
     * @return 構建器實例
     */
    NdsTransactionBuilder asset(AssetId asset);
    
    /**
     * 設置變更量
     * 
     * @param delta 變更量（必須，正數表示增加，負數表示減少）
     * @return 構建器實例
     */
    NdsTransactionBuilder delta(BigDecimal delta);
    
    /**
     * 設置一致性模式
     * 
     * @param consistency 一致性模式（必須）
     * @return 構建器實例
     */
    NdsTransactionBuilder consistency(ConsistencyMode consistency);
    
    /**
     * 設置源身份（可選，用於轉賬）
     * 
     * @param source 源身份
     * @return 構建器實例
     */
    NdsTransactionBuilder source(NdsIdentity source);
    
    /**
     * 設置目標身份（可選，用於轉賬）
     * 
     * @param target 目標身份
     * @return 構建器實例
     */
    NdsTransactionBuilder target(NdsIdentity target);
    
    /**
     * 設置交易原因（可選）
     * 
     * @param reason 交易原因
     * @return 構建器實例
     */
    NdsTransactionBuilder reason(String reason);
    
    /**
     * 設置模式版本
     * 
     * @param schemaVersion 模式版本（默認為 1）
     * @return 構建器實例
     */
    NdsTransactionBuilder schemaVersion(int schemaVersion);
    
    /**
     * 設置元數據
     * 
     * @param metadata 元數據映射（可為 null）
     * @return 構建器實例
     */
    NdsTransactionBuilder metadata(Map<String, String> metadata);
    
    /**
     * 構建交易事件
     * 
     * <p><b>⚠️ 重要：Payload 運行時類型檢查</b></p>
     * <p>構建時會自動創建 Payload（包含 asset, delta, consistency, source, target, reason 等字段），
     * 並進行運行時類型檢查（見 {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEventBuilder#build()}）。</p>
     * 
     * <p><b>自動構建的 Payload 結構：</b></p>
     * <ul>
     *   <li>asset: String (AssetId.fullId())</li>
     *   <li>delta: BigDecimal</li>
     *   <li>consistency: String (ConsistencyMode.name())</li>
     *   <li>source: String (NdsIdentity 的字符串表示，如果設置)</li>
     *   <li>target: String (NdsIdentity 的字符串表示，如果設置)</li>
     *   <li>reason: String (如果設置)</li>
     * </ul>
     * 
     * @return 不可變的交易事件對象
     * @throws IllegalStateException 如果必填字段缺失
     * @throws IllegalArgumentException 如果 Payload 包含違規類型
     */
    NdsTransaction build();
    
    /**
     * 創建新的交易構建器
     * 
     * @return 新的構建器實例
     */
    static NdsTransactionBuilder create() {
        // [Index] NDS-JAVA-TRANSACTIONBUILDER-900 [Trace] Implementation is provided by nds-core.
        throw new UnsupportedOperationException("NdsTransactionBuilder implementation must be provided by nds-core");
    }
}

