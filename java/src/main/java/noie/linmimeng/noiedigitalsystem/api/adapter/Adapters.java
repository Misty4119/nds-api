package noie.linmimeng.noiedigitalsystem.api.adapter;

/**
 * [Index] NDS-JAVA-ADAPTERS-000
 * [Semantic] Unified access point for all Proto ↔ Domain adapters.
 *
 * <pre>{@code
 * // [Index] NDS-JAVA-ADAPTERS-EX-001 [Semantic] Decimal conversion.
 * var protoDecimal = DecimalAdapter.toProto(BigDecimal.valueOf(100.5));
 * BigDecimal value = DecimalAdapter.fromProto(protoDecimal);
 *
 * // [Index] NDS-JAVA-ADAPTERS-EX-002 [Semantic] Identity conversion.
 * var proto = IdentityAdapter.toProto(domainIdentity);
 * NdsIdentity domain = IdentityAdapter.fromProto(proto);
 *
 * // [Index] NDS-JAVA-ADAPTERS-EX-003 [Semantic] Money (v3 fixed-point) conversion.
 * var money = MoneyAdapter.toProto("NDS", BigDecimal.valueOf(100.5));
 * BigDecimal amount = MoneyAdapter.fromProto(money);
 *
 * // [Index] NDS-JAVA-ADAPTERS-EX-004 [Semantic] v3 RequestContext.
 * var ctx = V3RequestContextAdapter.create("req-1", "idem-1");
 * }</pre>
 *
 * @since 2.0.0
 */
public final class Adapters {
    
    private Adapters() {
        // [Index] NDS-JAVA-ADAPTERS-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /** @return {@link DecimalAdapter} (static utility; call methods directly) */
    public static Class<DecimalAdapter> decimal() {
        return DecimalAdapter.class;
    }

    /**
     * @return {@link MoneyAdapter} — v3 fixed-point Money (static utility)
     * @since 3.0.0
     */
    public static Class<MoneyAdapter> money() {
        return MoneyAdapter.class;
    }

    /**
     * @return {@link V3RequestContextAdapter} (static utility)
     * @since 3.0.0
     */
    public static Class<V3RequestContextAdapter> v3RequestContext() {
        return V3RequestContextAdapter.class;
    }

    /**
     * @return {@link V3ErrorStatusAdapter} (static utility)
     * @since 3.0.0
     */
    public static Class<V3ErrorStatusAdapter> v3ErrorStatus() {
        return V3ErrorStatusAdapter.class;
    }

    /**
     * @return {@link V3IdentityV1Adapter} — v3 identity primitives (static utility)
     * @since 3.0.0
     */
    public static Class<V3IdentityV1Adapter> v3IdentityV1() {
        return V3IdentityV1Adapter.class;
    }

    /**
     * @return {@link V3EventV1Adapter} — v3 event streaming primitives (static utility)
     * @since 3.0.0
     */
    public static Class<V3EventV1Adapter> v3EventV1() {
        return V3EventV1Adapter.class;
    }

    /**
     * @return {@link V3SyncV1Adapter} — v3 sync streaming primitives (static utility)
     * @since 3.0.0
     */
    public static Class<V3SyncV1Adapter> v3SyncV1() {
        return V3SyncV1Adapter.class;
    }

    /** @return {@link IdentityAdapter} (static utility) */
    public static Class<IdentityAdapter> identity() {
        return IdentityAdapter.class;
    }

    /** @return {@link AssetAdapter} (static utility) */
    public static Class<AssetAdapter> asset() {
        return AssetAdapter.class;
    }

    /** @return {@link ContextAdapter} (static utility) */
    public static Class<ContextAdapter> context() {
        return ContextAdapter.class;
    }

    /** @return {@link EventAdapter} (static utility) */
    public static Class<EventAdapter> event() {
        return EventAdapter.class;
    }

    /** @return {@link TransactionAdapter} (static utility) */
    public static Class<TransactionAdapter> transaction() {
        return TransactionAdapter.class;
    }

    /**
     * @return {@link PolicyAdapter} (static utility)
     * @since 2.2.0
     */
    public static Class<PolicyAdapter> policy() {
        return PolicyAdapter.class;
    }

    /**
     * @return {@link AuditAdapter} (static utility)
     * @since 2.2.0
     */
    public static Class<AuditAdapter> audit() {
        return AuditAdapter.class;
    }

    /** @return {@link ResultAdapter} (static utility) */
    public static Class<ResultAdapter> result() {
        return ResultAdapter.class;
    }
}
