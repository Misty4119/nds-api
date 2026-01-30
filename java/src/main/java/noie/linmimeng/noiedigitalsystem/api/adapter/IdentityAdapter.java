package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Identity Adapter - NdsIdentity Domain ↔ Proto 轉換
 * 
 * <p>處理 NdsIdentity Domain 對象與 Proto 消息之間的雙向轉換。</p>
 * 
 * @since 2.0.0
 */
public final class IdentityAdapter {
    
    private IdentityAdapter() {
        // [Index] NDS-JAVA-IDENTITY-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * 將 Domain NdsIdentity 轉換為 Proto NdsIdentity
     * 
     * @param domain Domain NdsIdentity（可為 null）
     * @return Proto NdsIdentity，如果輸入為 null 則返回 null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.identity.NdsIdentity toProto(NdsIdentity domain) {
        if (domain == null) {
            return null;
        }
        
        noie.linmimeng.noiedigitalsystem.api.proto.identity.NdsIdentity.Builder builder = 
            noie.linmimeng.noiedigitalsystem.api.proto.identity.NdsIdentity.newBuilder()
                .setId(domain.id())
                .setType(toProtoType(domain.type()));
        
        if (domain.metadata() != null) {
            builder.putAllMetadata(domain.metadata());
        }

        if (domain.attachedPolicyIds() != null && !domain.attachedPolicyIds().isEmpty()) {
            builder.addAllAttachedPolicyIds(domain.attachedPolicyIds());
        }
        
        return builder.build();
    }
    
    /**
     * 將 Proto NdsIdentity 轉換為 Domain NdsIdentity
     * 
     * @param proto Proto NdsIdentity（可為 null）
     * @return Domain NdsIdentity，如果輸入為 null 則返回 null
     */
    public static NdsIdentity fromProto(noie.linmimeng.noiedigitalsystem.api.proto.identity.NdsIdentity proto) {
        if (proto == null || proto.getId().isEmpty()) {
            return null;
        }
        
        IdentityType type = fromProtoType(proto.getType());
        Map<String, String> metadata = new HashMap<>(proto.getMetadataMap());
        List<String> attachedPolicyIds = List.copyOf(proto.getAttachedPolicyIdsList());
        
        return NdsIdentity.of(proto.getId(), type)
            .withMetadata(metadata)
            .withAttachedPolicyIds(attachedPolicyIds);
    }
    
    /**
     * 將 Domain IdentityType 轉換為 Proto IdentityType
     * 
     * @param type Domain IdentityType
     * @return Proto IdentityType
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType toProtoType(IdentityType type) {
        if (type == null) {
            return noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_UNSPECIFIED;
        }
        
        return switch (type) {
            case PLAYER -> noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_PLAYER;
            case SYSTEM -> noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_SYSTEM;
            case AI -> noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_AI;
            case EXTERNAL -> noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_EXTERNAL;
            case UNKNOWN -> noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType.IDENTITY_TYPE_UNSPECIFIED;
        };
    }
    
    /**
     * 將 Proto IdentityType 轉換為 Domain IdentityType
     * 
     * @param protoType Proto IdentityType
     * @return Domain IdentityType
     */
    public static IdentityType fromProtoType(noie.linmimeng.noiedigitalsystem.api.proto.identity.IdentityType protoType) {
        if (protoType == null) {
            return IdentityType.UNKNOWN;
        }
        
        return switch (protoType) {
            case IDENTITY_TYPE_PLAYER -> IdentityType.PLAYER;
            case IDENTITY_TYPE_SYSTEM -> IdentityType.SYSTEM;
            case IDENTITY_TYPE_AI -> IdentityType.AI;
            case IDENTITY_TYPE_EXTERNAL -> IdentityType.EXTERNAL;
            case IDENTITY_TYPE_UNSPECIFIED, UNRECOGNIZED -> IdentityType.UNKNOWN;
        };
    }
}
