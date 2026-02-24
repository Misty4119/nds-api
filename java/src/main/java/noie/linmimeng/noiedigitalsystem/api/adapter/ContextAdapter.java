package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.context.NdsContext;
import java.util.Map;
import java.util.HashMap;

/**
 * [Index] NDS-JAVA-CONTEXT-000
 * [Semantic] NdsContext Domain ↔ Proto conversion.
 *
 * @since 2.0.0
 */
public final class ContextAdapter {
    
    private ContextAdapter() {
        // [Index] NDS-JAVA-CONTEXT-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * @param domain domain NdsContext (nullable)
     * @return Proto NdsContext; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.context.NdsContext toProto(NdsContext domain) {
        if (domain == null) {
            return null;
        }
        
        noie.linmimeng.noiedigitalsystem.api.proto.context.NdsContext.Builder builder = 
            noie.linmimeng.noiedigitalsystem.api.proto.context.NdsContext.newBuilder()
                .setTraceId(domain.traceId())
                .setCorrelationId(domain.correlationId())
                .setTimestamp(System.currentTimeMillis());
        
        if (domain.meta() != null) {
            builder.putAllMeta(domain.meta());
        }
        
        return builder.build();
    }
    
    /**
     * @param proto Proto NdsContext (nullable)
     * @return domain NdsContext; null if proto is null or traceId is empty
     */
    public static NdsContext fromProto(noie.linmimeng.noiedigitalsystem.api.proto.context.NdsContext proto) {
        if (proto == null || proto.getTraceId().isEmpty()) {
            return null;
        }
        
        String traceId = proto.getTraceId();
        String correlationId = proto.getCorrelationId().isEmpty() ? traceId : proto.getCorrelationId();
        Map<String, String> meta = new HashMap<>(proto.getMetaMap());
        
        // [Index] NDS-JAVA-CONTEXT-010 [Behavior] Construct via factory, then enrich with metadata.
        NdsContext context = NdsContext.create(traceId, correlationId);
        if (!meta.isEmpty()) {
            context = context.withMeta(meta);
        }
        return context;
    }
}
