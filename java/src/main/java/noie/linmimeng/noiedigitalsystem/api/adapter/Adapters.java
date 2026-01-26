package noie.linmimeng.noiedigitalsystem.api.adapter;

/**
 * Adapters - Proto ↔ Domain 轉換的統一入口
 * 
 * <p>提供便捷的靜態方法訪問所有 Adapter。</p>
 * 
 * <h2>使用範例</h2>
 * <pre>{@code
 * import static noie.linmimeng.noiedigitalsystem.api.adapter.Adapters.*;
 * 
 * // [Index] NDS-JAVA-ADAPTERS-EX-001 [Semantic] Decimal conversion.
 * Decimal protoDecimal = decimal().toProto(BigDecimal.valueOf(100.5));
 * BigDecimal javaBigDecimal = decimal().fromProto(protoDecimal);
 * 
 * // [Index] NDS-JAVA-ADAPTERS-EX-002 [Semantic] Identity conversion.
 * nds.identity.NdsIdentity protoIdentity = identity().toProto(domainIdentity);
 * NdsIdentity domainIdentity = identity().fromProto(protoIdentity);
 * }</pre>
 * 
 * @since 2.0.0
 */
public final class Adapters {
    
    private Adapters() {
        // [Index] NDS-JAVA-ADAPTERS-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * 獲取 DecimalAdapter 實例
     * 
     * @return DecimalAdapter 類（靜態方法）
     */
    public static Class<DecimalAdapter> decimal() {
        return DecimalAdapter.class;
    }
    
    /**
     * 獲取 IdentityAdapter 實例
     * 
     * @return IdentityAdapter 類（靜態方法）
     */
    public static Class<IdentityAdapter> identity() {
        return IdentityAdapter.class;
    }
    
    /**
     * 獲取 AssetAdapter 實例
     * 
     * @return AssetAdapter 類（靜態方法）
     */
    public static Class<AssetAdapter> asset() {
        return AssetAdapter.class;
    }
    
    /**
     * 獲取 ContextAdapter 實例
     * 
     * @return ContextAdapter 類（靜態方法）
     */
    public static Class<ContextAdapter> context() {
        return ContextAdapter.class;
    }
    
    /**
     * 獲取 EventAdapter 實例
     * 
     * @return EventAdapter 類（靜態方法）
     */
    public static Class<EventAdapter> event() {
        return EventAdapter.class;
    }
    
    /**
     * 獲取 TransactionAdapter 實例
     * 
     * @return TransactionAdapter 類（靜態方法）
     */
    public static Class<TransactionAdapter> transaction() {
        return TransactionAdapter.class;
    }
    
    /**
     * 獲取 ResultAdapter 實例
     * 
     * @return ResultAdapter 類（靜態方法）
     */
    public static Class<ResultAdapter> result() {
        return ResultAdapter.class;
    }
}
