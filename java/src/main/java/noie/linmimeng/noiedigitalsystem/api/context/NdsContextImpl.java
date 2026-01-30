package noie.linmimeng.noiedigitalsystem.api.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * [Index] NDS-JAVA-CONTEXT-IMPL-000
 * [Semantic] Default implementation of {@link NdsContext}.
 * 
 * @since 2.0.0
 */
final class NdsContextImpl implements NdsContext {
    private final String traceId;
    private final String correlationId;
    private final Map<String, String> meta;
    
    NdsContextImpl(String traceId, String correlationId, Map<String, String> meta) {
        if (traceId == null || traceId.isEmpty()) {
            throw new IllegalArgumentException("TraceId cannot be null or empty");
        }
        if (correlationId == null || correlationId.isEmpty()) {
            throw new IllegalArgumentException("CorrelationId cannot be null or empty");
        }
        this.traceId = traceId;
        this.correlationId = correlationId;
        this.meta = meta != null ? Collections.unmodifiableMap(new HashMap<>(meta)) : Map.of();
    }
    
    @Override
    public String traceId() {
        return traceId;
    }
    
    @Override
    public String correlationId() {
        return correlationId;
    }
    
    @Override
    public Map<String, String> meta() {
        return meta;
    }
    
    @Override
    public NdsContext withMeta(Map<String, String> additionalMeta) {
        Map<String, String> newMeta = new HashMap<>(this.meta);
        if (additionalMeta != null) {
            newMeta.putAll(additionalMeta);
        }
        return new NdsContextImpl(traceId, correlationId, newMeta);
    }
    
    @Override
    public NdsContext withMeta(String key, String value) {
        Map<String, String> newMeta = new HashMap<>(this.meta);
        newMeta.put(key, value);
        return new NdsContextImpl(traceId, correlationId, newMeta);
    }
    
    @Override
    public String toString() {
        return "NdsContext{traceId='" + traceId + "', correlationId='" + correlationId + "'}";
    }
}

