package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * [Index] NDS-JAVA-MODEL-PLAYERDIGITAL-000
 * [Semantic] Player-scoped Digital instance.
 * 
 * <p>[Constraint] All numeric values must use {@link BigDecimal}.</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerDigital {
    
    private UUID playerUUID;
    private String digitalName;
    private BigDecimal amount;
    private BigDecimal limit; // [Index] NDS-JAVA-MODEL-PLAYERDIGITAL-001 [Semantic] null => no limit.
    
    /**
     * Create a player Digital with no limit.
     *
     * @param playerUUID player UUID
     * @param digitalName Digital name
     * @param amount initial value
     */
    public PlayerDigital(UUID playerUUID, String digitalName, BigDecimal amount) {
        this.playerUUID = playerUUID;
        this.digitalName = digitalName;
        this.amount = amount;
        this.limit = null; // [Index] NDS-JAVA-MODEL-PLAYERDIGITAL-002 [Semantic] No limit.
    }
    
    /**
     * @return true if a limit is configured; false if unlimited
     */
    public boolean hasLimit() {
        return limit != null;
    }
}

