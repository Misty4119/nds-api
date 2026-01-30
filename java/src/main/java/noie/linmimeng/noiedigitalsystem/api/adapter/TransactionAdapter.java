package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.transaction.ConsistencyMode;

/**
 * Transaction Adapter - Transaction Domain ↔ Proto 轉換
 * 
 * <p>處理交易相關 Domain 對象與 Proto 消息之間的雙向轉換。</p>
 * 
 * @since 2.0.0
 */
public final class TransactionAdapter {
    
    private TransactionAdapter() {
        // [Index] NDS-JAVA-TRANSACTION-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-TRANSACTION-010 [Semantic] ConsistencyMode conversion.
    // ========================================================================
    
    /**
     * 將 Domain ConsistencyMode 轉換為 Proto ConsistencyMode
     * 
     * @param mode Domain ConsistencyMode
     * @return Proto ConsistencyMode
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode toProtoMode(ConsistencyMode mode) {
        if (mode == null) {
            return noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode.CONSISTENCY_MODE_STRONG;
        }
        
        return switch (mode) {
            case STRONG -> noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode.CONSISTENCY_MODE_STRONG;
            case EVENTUAL -> noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode.CONSISTENCY_MODE_EVENTUAL;
            case OPTIMISTIC -> noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode.CONSISTENCY_MODE_OPTIMISTIC;
        };
    }
    
    /**
     * 將 Proto ConsistencyMode 轉換為 Domain ConsistencyMode
     * 
     * @param protoMode Proto ConsistencyMode
     * @return Domain ConsistencyMode
     */
    public static ConsistencyMode fromProtoMode(noie.linmimeng.noiedigitalsystem.api.proto.transaction.ConsistencyMode protoMode) {
        if (protoMode == null) {
            return ConsistencyMode.STRONG;
        }
        
        return switch (protoMode) {
            case CONSISTENCY_MODE_STRONG, CONSISTENCY_MODE_UNSPECIFIED -> ConsistencyMode.STRONG;
            case CONSISTENCY_MODE_EVENTUAL -> ConsistencyMode.EVENTUAL;
            case CONSISTENCY_MODE_OPTIMISTIC -> ConsistencyMode.OPTIMISTIC;
            case UNRECOGNIZED -> ConsistencyMode.STRONG;
        };
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-TRANSACTION-900 [Semantic] NdsTransaction conversion.
    // [Trace] Full conversion requires payload/context handling and is implemented in runtime modules when needed.
    // ========================================================================
    
    // [Index] NDS-JAVA-TRANSACTION-901 [TODO] Implement full NdsTransaction <-> proto conversion when required.
}
