package noie.linmimeng.noiedigitalsystem.api.audit;

import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [Index] NDS-JAVA-AUDIT-IMPL-000
 * [Semantic] Default immutable {@link NdsRationale} implementation.
 *
 * @since 2.2.0
 */
final class NdsRationaleImpl implements NdsRationale {
    private final String source;
    private final BigDecimal confidence;
    private final List<String> thoughtPath;
    private final List<EventId> evidenceEventIds;
    private final List<NdsRationaleRef> evidenceRefs;
    private final BigDecimal riskScore;
    private final Map<String, String> metadata;

    NdsRationaleImpl(
        String source,
        BigDecimal confidence,
        List<String> thoughtPath,
        List<EventId> evidenceEventIds,
        List<NdsRationaleRef> evidenceRefs,
        BigDecimal riskScore,
        Map<String, String> metadata
    ) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("source cannot be null or empty");
        }
        this.source = source;
        this.confidence = confidence; // optional
        this.thoughtPath = thoughtPath != null ? List.copyOf(thoughtPath) : List.of();
        this.evidenceEventIds = evidenceEventIds != null ? List.copyOf(evidenceEventIds) : List.of();
        this.evidenceRefs = evidenceRefs != null ? List.copyOf(evidenceRefs) : List.of();
        this.riskScore = riskScore; // optional
        this.metadata = metadata != null ? Collections.unmodifiableMap(new HashMap<>(metadata)) : Map.of();
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public BigDecimal confidence() {
        return confidence;
    }

    @Override
    public List<String> thoughtPath() {
        return thoughtPath;
    }

    @Override
    public List<EventId> evidenceEventIds() {
        return evidenceEventIds;
    }

    @Override
    public List<NdsRationaleRef> evidenceRefs() {
        return evidenceRefs;
    }

    @Override
    public BigDecimal riskScore() {
        return riskScore;
    }

    @Override
    public Map<String, String> metadata() {
        return metadata;
    }
}

