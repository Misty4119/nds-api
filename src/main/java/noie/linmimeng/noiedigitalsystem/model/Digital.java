package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Digital 模型類
 * 
 * <p>用於表示全服 Digital 或全域玩家 Digital 定義。</p>
 * <p><b>注意：</b>所有數值使用 BigDecimal 確保精度，符合 NDS 協議要求。</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class Digital {
    
    private String name;
    private BigDecimal amount;
    private BigDecimal limit; // null 表示無限制
    
    /**
     * 創建無限制的 Digital
     * @param name Digital 名稱
     * @param amount 數值
     */
    public Digital(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
        this.limit = null; // 無限制
    }
    
    /**
     * 檢查是否有上限
     * @return true 如果有上限，false 如果無限制
     */
    public boolean hasLimit() {
        return limit != null;
    }
}

