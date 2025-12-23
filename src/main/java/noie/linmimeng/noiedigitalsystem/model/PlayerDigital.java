package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 玩家 Digital 模型類
 * 
 * <p>用於表示玩家專屬的 Digital 實例。</p>
 * <p><b>注意：</b>所有數值使用 BigDecimal 確保精度，符合 NDS 協議要求。</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerDigital {
    
    private UUID playerUUID;
    private String digitalName;
    private BigDecimal amount;
    private BigDecimal limit; // null 表示無限制
    
    /**
     * 創建無限制的玩家 Digital
     * @param playerUUID 玩家 UUID
     * @param digitalName Digital 名稱
     * @param amount 數值
     */
    public PlayerDigital(UUID playerUUID, String digitalName, BigDecimal amount) {
        this.playerUUID = playerUUID;
        this.digitalName = digitalName;
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

