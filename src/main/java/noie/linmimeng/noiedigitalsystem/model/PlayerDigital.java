package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * 玩家 Digital 模型類
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerDigital {
    
    private UUID playerUUID;
    private String digitalName;
    private double amount;
    private double limit;
    
    public PlayerDigital(UUID playerUUID, String digitalName, double amount) {
        this.playerUUID = playerUUID;
        this.digitalName = digitalName;
        this.amount = amount;
        this.limit = -1; // 無限制
    }
}

