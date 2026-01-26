package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.proto.common.Decimal;
import java.math.BigDecimal;

/**
 * Decimal Adapter - BigDecimal ↔ Proto Decimal 轉換
 * 
 * <p>處理 Java BigDecimal 與 Proto Decimal 之間的精確轉換。</p>
 * 
 * <p><b>重要</b>：Proto 使用字符串表示 BigDecimal 以確保精度不丟失。</p>
 * 
 * @since 2.0.0
 */
public final class DecimalAdapter {
    
    private DecimalAdapter() {
        // [Index] NDS-JAVA-DECIMAL-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * 將 Java BigDecimal 轉換為 Proto Decimal
     * 
     * @param value Java BigDecimal（可為 null）
     * @return Proto Decimal，如果輸入為 null 則返回 null
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
     * 將 Proto Decimal 轉換為 Java BigDecimal
     * 
     * @param proto Proto Decimal（可為 null）
     * @return Java BigDecimal，如果輸入為 null 則返回 null
     */
    public static BigDecimal fromProto(Decimal proto) {
        if (proto == null || proto.getValue().isEmpty()) {
            return null;
        }
        return new BigDecimal(proto.getValue());
    }
    
    /**
     * 將 Proto Decimal 轉換為 Java BigDecimal（帶默認值）
     * 
     * @param proto Proto Decimal（可為 null）
     * @param defaultValue 默認值
     * @return Java BigDecimal，如果輸入為 null 則返回默認值
     */
    public static BigDecimal fromProto(Decimal proto, BigDecimal defaultValue) {
        BigDecimal result = fromProto(proto);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 創建表示零的 Proto Decimal
     * 
     * @return 表示零的 Proto Decimal
     */
    public static Decimal zero() {
        return Decimal.newBuilder()
            .setValue("0")
            .setScale(0)
            .build();
    }
}
