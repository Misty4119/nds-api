package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import java.math.BigDecimal;

/**
 * [Index] NDS-JAVA-TRANSACTION-000
 * [Semantic] Specialized event representing an asset delta operation.
 *
 * <p>[Constraint] Transaction is a subtype of NdsEvent; all economic operations must be expressed as transactions.</p>
 * <p>[Constraint] delta is positive for credit, negative for debit.</p>
 *
 * @since 2.0.0
 */
public interface NdsTransaction extends NdsEvent {

    /** @return asset subject to the delta */
    AssetId asset();

    /**
     * @return BigDecimal delta (positive = credit, negative = debit; never null)
     */
    BigDecimal delta();

    /** @return consistency requirement for applying this transaction */
    ConsistencyMode consistency();

    /**
     * @return source identity for transfers; null if not set
     */
    default NdsIdentity source() {
        return payload().getString("source") != null
            ? NdsIdentity.fromString(payload().getString("source"))
            : null;
    }

    /**
     * @return target identity for transfers; null if not set
     */
    default NdsIdentity target() {
        return payload().getString("target") != null
            ? NdsIdentity.fromString(payload().getString("target"))
            : null;
    }

    /** @return human-readable reason; null if not set */
    default String reason() {
        return payload().getString("reason");
    }

    @Override
    default boolean isValid() {
        return NdsEvent.super.isValid()
            && asset() != null
            && delta() != null
            && consistency() != null;
    }
}

