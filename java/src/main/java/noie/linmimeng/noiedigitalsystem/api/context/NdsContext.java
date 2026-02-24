package noie.linmimeng.noiedigitalsystem.api.context;

import java.util.Map;

/**
 * [Index] NDS-JAVA-CONTEXT-000
 * [Semantic] Immutable tracing and correlation context for distributed request tracking.
 *
 * <p>[Constraint] Implementations must be immutable.</p>
 *
 * @since 2.0.0
 */
public interface NdsContext {

    /** @return globally unique trace ID (UUID format) */
    String traceId();

    /** @return correlation ID; may equal traceId or identify a logical operation group */
    String correlationId();

    /** @return supplementary metadata (never null; may be empty) */
    Map<String, String> meta();

    /**
     * @param key metadata key
     * @return metadata value; null if key is absent
     */
    default String getMeta(String key) {
        return meta().get(key);
    }

    /**
     * @param additionalMeta entries to merge into a new context
     * @return new NdsContext with merged metadata
     */
    NdsContext withMeta(Map<String, String> additionalMeta);

    /**
     * @param key metadata key
     * @param value metadata value
     * @return new NdsContext with the added entry
     */
    NdsContext withMeta(String key, String value);

    /**
     * @param traceId trace ID; auto-generated UUID if null
     * @param correlationId correlation ID; defaults to traceId if null
     * @return new NdsContext
     */
    static NdsContext create(String traceId, String correlationId) {
        return new NdsContextImpl(
            traceId != null ? traceId : java.util.UUID.randomUUID().toString(),
            correlationId != null ? correlationId : traceId,
            Map.of()
        );
    }

    /** @return new NdsContext with auto-generated traceId and correlationId */
    static NdsContext create() {
        String traceId = java.util.UUID.randomUUID().toString();
        return create(traceId, traceId);
    }
}

