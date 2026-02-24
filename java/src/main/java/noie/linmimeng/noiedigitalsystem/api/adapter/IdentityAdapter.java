package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.identity.IdentityType;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * [Index] NDS-JAVA-IDENTITY-000
 * [Semantic] NdsIdentity / IdentityType Domain ↔ Proto conversion.
 *
 * @since 2.0.0
 */
public final class IdentityAdapter {
    
    private IdentityAdapter() {
        // [Index] NDS-JAVA-IDENTITY-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    /**
     * @param domain domain NdsIdentity (nullable)
     * @return Proto NdsIdentity; null if input is null
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
     * @param proto Proto NdsIdentity (nullable)
     * @return domain NdsIdentity; null if proto is null or id is empty
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
     * @param type domain IdentityType (nullable; treated as UNKNOWN if null)
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
     * @param protoType Proto IdentityType (nullable; treated as UNKNOWN if null)
     * @return domain IdentityType
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
