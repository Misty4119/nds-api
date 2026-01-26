package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.asset.AssetScope;
import noie.linmimeng.noiedigitalsystem.api.asset.NdsAsset;
import java.util.Map;
import java.util.HashMap;

/**
 * Asset Adapter - AssetId/NdsAsset Domain ↔ Proto 轉換
 * 
 * <p>處理資產相關 Domain 對象與 Proto 消息之間的雙向轉換。</p>
 * 
 * @since 2.0.0
 */
public final class AssetAdapter {
    
    private AssetAdapter() {
        // [Index] NDS-JAVA-ASSET-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-ASSET-010 [Semantic] AssetId conversion.
    // ========================================================================
    
    /**
     * 將 Domain AssetId 轉換為 Proto AssetId
     * 
     * @param domain Domain AssetId（可為 null）
     * @return Proto AssetId，如果輸入為 null 則返回 null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetId toProto(AssetId domain) {
        if (domain == null) {
            return null;
        }
        
        return noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetId.newBuilder()
            .setName(domain.name())
            .setScope(toProtoScope(domain.scope()))
            .build();
    }
    
    /**
     * 將 Proto AssetId 轉換為 Domain AssetId
     * 
     * @param proto Proto AssetId（可為 null）
     * @return Domain AssetId，如果輸入為 null 則返回 null
     */
    public static AssetId fromProto(noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetId proto) {
        if (proto == null || proto.getName().isEmpty()) {
            return null;
        }
        
        AssetScope scope = fromProtoScope(proto.getScope());
        return AssetId.of(scope, proto.getName());
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-ASSET-020 [Semantic] AssetScope conversion.
    // ========================================================================
    
    /**
     * 將 Domain AssetScope 轉換為 Proto AssetScope
     * 
     * @param scope Domain AssetScope
     * @return Proto AssetScope
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope toProtoScope(AssetScope scope) {
        if (scope == null) {
            return noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope.ASSET_SCOPE_UNSPECIFIED;
        }
        
        return switch (scope) {
            case PLAYER -> noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope.ASSET_SCOPE_PLAYER;
            case SERVER -> noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope.ASSET_SCOPE_SERVER;
            case GLOBAL -> noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope.ASSET_SCOPE_GLOBAL;
            case UNKNOWN -> noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope.ASSET_SCOPE_UNSPECIFIED;
        };
    }
    
    /**
     * 將 Proto AssetScope 轉換為 Domain AssetScope
     * 
     * @param protoScope Proto AssetScope
     * @return Domain AssetScope
     */
    public static AssetScope fromProtoScope(noie.linmimeng.noiedigitalsystem.api.proto.asset.AssetScope protoScope) {
        if (protoScope == null) {
            return AssetScope.UNKNOWN;
        }
        
        return switch (protoScope) {
            case ASSET_SCOPE_PLAYER -> AssetScope.PLAYER;
            case ASSET_SCOPE_SERVER -> AssetScope.SERVER;
            case ASSET_SCOPE_GLOBAL -> AssetScope.GLOBAL;
            case ASSET_SCOPE_UNSPECIFIED, UNRECOGNIZED -> AssetScope.UNKNOWN;
        };
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-ASSET-900 [Semantic] NdsAsset conversion.
    // [Trace] Reserved for future expansion.
    // ========================================================================
    
    // [Index] NDS-JAVA-ASSET-901 [TODO] Implement full NdsAsset <-> proto conversion when required.
    // public static noie.linmimeng.noiedigitalsystem.api.proto.asset.NdsAsset toProto(NdsAsset domain)
    // public static NdsAsset fromProto(noie.linmimeng.noiedigitalsystem.api.proto.asset.NdsAsset proto)
}
