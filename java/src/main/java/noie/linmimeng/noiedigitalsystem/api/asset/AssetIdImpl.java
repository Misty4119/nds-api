package noie.linmimeng.noiedigitalsystem.api.asset;

/**
 * [Index] NDS-JAVA-ASSETID-IMPL-000
 * [Semantic] Default implementation of {@link AssetId}.
 * 
 * @since 2.0.0
 */
final class AssetIdImpl implements AssetId {
    private final AssetScope scope;
    private final String name;
    private final String fullId;
    
    AssetIdImpl(AssetScope scope, String name) {
        if (scope == null) {
            throw new IllegalArgumentException("AssetScope cannot be null");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Asset name cannot be null or empty");
        }
        this.scope = scope;
        this.name = name;
        this.fullId = scope.name().toLowerCase() + ":" + name;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public AssetScope scope() {
        return scope;
    }
    
    @Override
    public String fullId() {
        return fullId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetIdImpl assetId = (AssetIdImpl) o;
        return fullId.equals(assetId.fullId);
    }
    
    @Override
    public int hashCode() {
        return fullId.hashCode();
    }
    
    @Override
    public String toString() {
        return "AssetId{fullId='" + fullId + "'}";
    }
}

