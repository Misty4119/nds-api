package noie.linmimeng.noiedigitalsystem.api.audit;

import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * [Index] NDS-JAVA-AUDIT-000
 * [Semantic] Structured rationale for audit / explainability (lightweight).
 *
 * <p>[Constraint] This is not a full chain-of-thought; keep it auditable and compact.</p>
 *
 * @since 2.2.0
 */
public interface NdsRationale {

    String source();

    /**
     * Optional confidence score in [0,1].
     */
    BigDecimal confidence();

    List<String> thoughtPath();

    List<EventId> evidenceEventIds();

    List<NdsRationaleRef> evidenceRefs();

    /**
     * Optional risk indicator (semantics/range are implementation-defined).
     */
    BigDecimal riskScore();

    Map<String, String> metadata();

    static NdsRationale of(
        String source,
        BigDecimal confidence,
        List<String> thoughtPath,
        List<EventId> evidenceEventIds,
        List<NdsRationaleRef> evidenceRefs,
        BigDecimal riskScore,
        Map<String, String> metadata
    ) {
        return new NdsRationaleImpl(source, confidence, thoughtPath, evidenceEventIds, evidenceRefs, riskScore, metadata);
    }
}

