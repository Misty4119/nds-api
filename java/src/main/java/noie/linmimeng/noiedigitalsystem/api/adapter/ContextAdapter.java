package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.context.NdsContext;
import java.util.Map;
import java.util.HashMap;

/**
 * Context Adapter - NdsContext Domain ↔ Proto 轉換
 * 
 * <p>處理 NdsContext Domain 對象與 Proto 消息之間的雙向轉換。</p>
 * 
 * @since 2.0.0
 */
public final class ContextAdapter {
    
    private ContextAdapter() {
        // [Index] NDS-JAVA-CONTEXT-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * 將 Domain NdsContext 轉換為 Proto NdsContext
     * 
     * @param domain Domain NdsContext（可為 null）
     * @return Proto NdsContext，如果輸入為 null 則返回 null
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
     * 將 Proto NdsContext 轉換為 Domain NdsContext
     * 
     * @param proto Proto NdsContext（可為 null）
     * @return Domain NdsContext，如果輸入為 null 則返回 null
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
