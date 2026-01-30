package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import noie.linmimeng.noiedigitalsystem.api.result.NdsError;
import java.util.Map;
import java.util.HashMap;

/**
 * Result Adapter - NdsResult/NdsError Domain ↔ Proto 轉換
 * 
 * <p>處理結果相關 Domain 對象與 Proto 消息之間的雙向轉換。</p>
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
     * 將 Domain NdsResult 轉換為 Proto NdsResult
     * 
     * @param domain Domain NdsResult（可為 null）
     * @return Proto NdsResult，如果輸入為 null 則返回 null
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
    
    /**
     * 創建成功的 Proto NdsResult
     * 
     * @return 成功的 Proto NdsResult
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult successProto() {
        return noie.linmimeng.noiedigitalsystem.api.proto.common.NdsResult.newBuilder()
            .setSuccess(true)
            .build();
    }
    
    /**
     * 創建失敗的 Proto NdsResult
     * 
     * @param code 錯誤碼
     * @param message 錯誤消息
     * @return 失敗的 Proto NdsResult
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
     * 將 Domain NdsError 轉換為 Proto NdsError
     * 
     * @param domain Domain NdsError（可為 null）
     * @return Proto NdsError，如果輸入為 null 則返回 null
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
     * 將 Proto NdsError 轉換為 Domain NdsError
     * 
     * @param proto Proto NdsError（可為 null）
     * @return Domain NdsError，如果輸入為 null 則返回 null
     */
    public static NdsError fromProto(noie.linmimeng.noiedigitalsystem.api.proto.common.NdsError proto) {
        if (proto == null || proto.getCode().isEmpty()) {
            return null;
        }
        
        Map<String, Object> details = new HashMap<>(proto.getDetailsMap());
        return NdsError.of(proto.getCode(), proto.getMessage(), details);
    }
}
