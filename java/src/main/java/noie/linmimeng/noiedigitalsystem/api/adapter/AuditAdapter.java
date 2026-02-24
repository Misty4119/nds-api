package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.audit.NdsRationale;
import noie.linmimeng.noiedigitalsystem.api.audit.NdsRationaleRef;
import noie.linmimeng.noiedigitalsystem.api.event.EventId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [Index] NDS-JAVA-AUDIT-000
 * [Semantic] NdsRationale / NdsRationaleRef Domain ↔ Proto conversion.
 *
 * @since 2.2.0
 */
public final class AuditAdapter {

    private AuditAdapter() {
        // [Index] NDS-JAVA-AUDIT-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * @param domain domain rationale ref (nullable)
     * @return Proto NdsRationaleRef; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationaleRef toProto(NdsRationaleRef domain) {
        if (domain == null) return null;

        var builder = noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationaleRef.newBuilder()
            .setUri(domain.uri());

        if (domain.hash() != null && !domain.hash().isEmpty()) {
            builder.setHash(domain.hash());
        }
        if (domain.mimeType() != null && !domain.mimeType().isEmpty()) {
            builder.setMimeType(domain.mimeType());
        }

        return builder.build();
    }

    /**
     * @param proto Proto NdsRationaleRef (nullable)
     * @return domain NdsRationaleRef; null if proto is null or uri is empty
     */
    public static NdsRationaleRef fromProto(noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationaleRef proto) {
        if (proto == null || proto.getUri().isEmpty()) return null;
        return NdsRationaleRef.of(
            proto.getUri(),
            proto.hasHash() ? proto.getHash() : "",
            proto.hasMimeType() ? proto.getMimeType() : ""
        );
    }

    /**
     * @param domain domain NdsRationale (nullable)
     * @return Proto NdsRationale; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationale toProto(NdsRationale domain) {
        if (domain == null) return null;

        var builder = noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationale.newBuilder()
            .setSource(domain.source());

        BigDecimal confidence = domain.confidence();
        if (confidence != null) {
            builder.setConfidence(DecimalAdapter.toProto(confidence));
        }

        if (domain.thoughtPath() != null) {
            builder.addAllThoughtPath(domain.thoughtPath());
        }

        if (domain.evidenceEventIds() != null && !domain.evidenceEventIds().isEmpty()) {
            List<noie.linmimeng.noiedigitalsystem.api.proto.event.EventId> ids = domain.evidenceEventIds()
                .stream()
                .map(EventAdapter::toProto)
                .toList();
            builder.addAllEvidenceEventIds(ids);
        }

        if (domain.evidenceRefs() != null && !domain.evidenceRefs().isEmpty()) {
            List<noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationaleRef> refs = domain.evidenceRefs()
                .stream()
                .map(AuditAdapter::toProto)
                .toList();
            builder.addAllEvidenceRefs(refs);
        }

        BigDecimal riskScore = domain.riskScore();
        if (riskScore != null) {
            builder.setRiskScore(DecimalAdapter.toProto(riskScore));
        }

        if (domain.metadata() != null) {
            builder.putAllMetadata(domain.metadata());
        }

        return builder.build();
    }

    /**
     * @param proto Proto NdsRationale (nullable)
     * @return domain NdsRationale; null if proto is null or source is empty
     */
    public static NdsRationale fromProto(noie.linmimeng.noiedigitalsystem.api.proto.audit.NdsRationale proto) {
        if (proto == null || proto.getSource().isEmpty()) return null;

        BigDecimal confidence = proto.hasConfidence() ? DecimalAdapter.fromProto(proto.getConfidence()) : null;
        BigDecimal riskScore = proto.hasRiskScore() ? DecimalAdapter.fromProto(proto.getRiskScore()) : null;

        List<String> thoughtPath = new ArrayList<>(proto.getThoughtPathList());

        List<EventId> evidenceEventIds = proto.getEvidenceEventIdsList()
            .stream()
            .map(EventAdapter::fromProto)
            .filter(e -> e != null)
            .toList();

        List<NdsRationaleRef> evidenceRefs = proto.getEvidenceRefsList()
            .stream()
            .map(AuditAdapter::fromProto)
            .filter(r -> r != null)
            .toList();

        Map<String, String> metadata = new HashMap<>(proto.getMetadataMap());

        return NdsRationale.of(
            proto.getSource(),
            confidence,
            thoughtPath,
            evidenceEventIds,
            evidenceRefs,
            riskScore,
            metadata
        );
    }
}

