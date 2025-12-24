package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import java.math.BigDecimal;

/**
 * NDS Transaction - 經濟事件
 * 
 * <p>代表資產轉移的經濟事件。所有經濟操作都必須通過 Transaction 來表達。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>Transaction 是 Event 的特殊類型</li>
 *   <li>必須支持一致性模式</li>
 *   <li>delta 可以是正數（增加）或負數（減少）</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsTransaction extends NdsEvent {
    
    /**
     * 獲取資產 ID
     * 
     * @return 資產 ID
     */
    AssetId asset();
    
    /**
     * 獲取變更量
     * 
     * <p>正數表示增加，負數表示減少。</p>
     * 
     * @return 變更量（BigDecimal，確保精度）
     */
    BigDecimal delta();
    
    /**
     * 獲取一致性模式
     * 
     * @return 一致性模式
     */
    ConsistencyMode consistency();
    
    /**
     * 獲取源身份（可選）
     * 
     * <p>對於轉賬操作，這是發送方。</p>
     * 
     * @return 源身份，如果不存在則返回 null
     */
    default NdsIdentity source() {
        return payload().getString("source") != null 
            ? NdsIdentity.fromString(payload().getString("source"))
            : null;
    }
    
    /**
     * 獲取目標身份（可選）
     * 
     * <p>對於轉賬操作，這是接收方。</p>
     * 
     * @return 目標身份，如果不存在則返回 null
     */
    default NdsIdentity target() {
        return payload().getString("target") != null 
            ? NdsIdentity.fromString(payload().getString("target"))
            : null;
    }
    
    /**
     * 獲取交易原因
     * 
     * @return 交易原因（可為 null）
     */
    default String reason() {
        return payload().getString("reason");
    }
    
    /**
     * 檢查交易是否有效
     * 
     * @return true 如果交易有效
     */
    @Override
    default boolean isValid() {
        return NdsEvent.super.isValid() 
            && asset() != null 
            && delta() != null 
            && consistency() != null;
    }
}

