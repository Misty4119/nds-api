package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import noie.linmimeng.noiedigitalsystem.api.result.NdsError;
import java.util.Map;
import java.util.HashMap;

/**
 * [Index] NDS-JAVA-RESULT-000
 * [Semantic] NdsResult / NdsError Domain ↔ Proto conversion.
 *
 * @since 2.0.0
 */
public final class ResultAdapter {
    
    private ResultAdapter() {
        // [Index] NDS-JAVA-RESULT-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-RESULT-010 [Semantic] NdsResult conversion.
    // ========================================================================
    
    /**
     * @param domain domain NdsResult (nullable)
     * @return Proto NdsResult; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult toProto(NdsResult<?> domain) {
        if (domain == null) {
            return null;
        }
        
        noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult.Builder builder = 
            noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult.newBuilder()
                .setSuccess(domain.isSuccess());
        
        if (!domain.isSuccess() && domain.error() != null) {
            builder.setError(toProto(domain.error()));
        }
        
        return builder.build();
    }
    
    /** @return Proto NdsResult with success=true */
    public static noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult successProto() {
        return noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult.newBuilder()
            .setSuccess(true)
            .build();
    }
    
    /**
     * @param code error code
     * @param message error message
     * @return Proto NdsResult with success=false and embedded error
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult failureProto(String code, String message) {
        return noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult.newBuilder()
            .setSuccess(false)
            .setError(noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError.newBuilder()
                .setCode(code)
                .setMessage(message)
                .build())
            .build();
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-RESULT-020 [Semantic] NdsError conversion.
    // ========================================================================
    
    /**
     * @param domain domain NdsError (nullable)
     * @return Proto NdsError; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError toProto(NdsError domain) {
        if (domain == null) {
            return null;
        }
        
        noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError.Builder builder = 
            noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError.newBuilder()
                .setCode(domain.code())
                .setMessage(domain.message());
        
        if (domain.details() != null) {
            // [Index] NDS-JAVA-RESULT-021 [Behavior] Normalize Map<String, Object> into Map<String, String>.
            Map<String, String> stringDetails = new HashMap<>();
            for (Map.Entry<String, Object> entry : domain.details().entrySet()) {
                stringDetails.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            builder.putAllDetails(stringDetails);
        }
        
        return builder.build();
    }
    
    /**
     * @param proto Proto NdsError (nullable)
     * @return domain NdsError; null if proto is null or code is empty
     */
    public static NdsError fromProto(noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError proto) {
        if (proto == null || proto.getCode().isEmpty()) {
            return null;
        }
        
        Map<String, Object> details = new HashMap<>(proto.getDetailsMap());
        return NdsError.of(proto.getCode(), proto.getMessage(), details);
    }
}
