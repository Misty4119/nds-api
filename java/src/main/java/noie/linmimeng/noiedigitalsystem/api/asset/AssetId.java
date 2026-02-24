package noie.linmimeng.noiedigitalsystem.api.asset;

/**
 * [Index] NDS-JAVA-ASSETID-000
 * [Semantic] Globally unique asset identifier (scope:name).
 *
 * @since 2.0.0
 */
public interface AssetId {

    /** @return asset name (e.g. "coins", "gold") */
    String name();

    /** @return canonical full ID string in "scope:name" format (e.g. "player:coins") */
    String fullId();

    /** @return ownership scope */
    AssetScope scope();

    /**
     * @param fullId non-empty "scope:name" string
     * @return parsed AssetId
     * @throws IllegalArgumentException if fullId is null, empty, or malformed
     */
    static AssetId fromString(String fullId) {
        if (fullId == null || fullId.isEmpty()) {
            throw new IllegalArgumentException("AssetId cannot be null or empty");
        }
        int colonIndex = fullId.indexOf(':');
        if (colonIndex <= 0 || colonIndex >= fullId.length() - 1) {
            throw new IllegalArgumentException("Invalid AssetId format: " + fullId);
        }
        String scopeStr = fullId.substring(0, colonIndex);
        String name = fullId.substring(colonIndex + 1);
        AssetScope scope = AssetScope.fromString(scopeStr);
        return of(scope, name);
    }

    /**
     * @param scope ownership scope
     * @param name asset name
     * @return AssetId instance
     */
    static AssetId of(AssetScope scope, String name) {
        return new AssetIdImpl(scope, name);
    }
}

