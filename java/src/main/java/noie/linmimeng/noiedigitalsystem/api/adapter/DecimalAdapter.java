package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.proto.common.Decimal;
import java.math.BigDecimal;

/**
 * [Index] NDS-JAVA-DECIMAL-000
 * [Semantic] BigDecimal ↔ Proto Decimal exact conversion.
 *
 * <p>[Constraint] Proto encodes decimals as plain strings to preserve full precision across languages.</p>
 *
 * @since 2.0.0
 */
public final class DecimalAdapter {
    
    private DecimalAdapter() {
        // [Index] NDS-JAVA-DECIMAL-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * @param value BigDecimal (nullable)
     * @return Proto Decimal; null if input is null
     */
    public static Decimal toProto(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return Decimal.newBuilder()
            .setValue(value.toPlainString())
            .setScale(value.scale())
            .build();
    }
    
    /**
     * @param proto Proto Decimal (nullable)
     * @return BigDecimal; null if input is null or value is empty
     */
    public static BigDecimal fromProto(Decimal proto) {
        if (proto == null || proto.getValue().isEmpty()) {
            return null;
        }
        return new BigDecimal(proto.getValue());
    }
    
    /**
     * @param proto Proto Decimal (nullable)
     * @param defaultValue value returned when proto is null or empty
     * @return BigDecimal; defaultValue if proto is null or empty
     */
    public static BigDecimal fromProto(Decimal proto, BigDecimal defaultValue) {
        BigDecimal result = fromProto(proto);
        return result != null ? result : defaultValue;
    }
    
    /** @return Proto Decimal representing zero (value="0", scale=0) */
    public static Decimal zero() {
        return Decimal.newBuilder()
            .setValue("0")
            .setScale(0)
            .build();
    }
}
