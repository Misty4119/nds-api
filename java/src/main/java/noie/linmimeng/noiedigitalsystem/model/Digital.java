package noie.linmimeng.noiedigitalsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * [Index] NDS-JAVA-MODEL-DIGITAL-000
 * [Semantic] Digital definition (server/global scope).
 * 
 * <p>[Constraint] All numeric values must use {@link BigDecimal}.</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class Digital {
    
    private String name;
    private BigDecimal amount;
    private BigDecimal limit; // [Index] NDS-JAVA-MODEL-DIGITAL-001 [Semantic] null => no limit.
    
    /**
     * Create a Digital with no limit.
     *
     * @param name Digital name
     * @param amount initial value
     */
    public Digital(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
        this.limit = null; // [Index] NDS-JAVA-MODEL-DIGITAL-002 [Semantic] No limit.
    }
    
    /**
     * @return true if a limit is configured; false if unlimited
     */
    public boolean hasLimit() {
        return limit != null;
    }
}

