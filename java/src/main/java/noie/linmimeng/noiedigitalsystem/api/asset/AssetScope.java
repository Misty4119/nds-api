package noie.linmimeng.noiedigitalsystem.api.asset;

/**
 * [Index] NDS-JAVA-ASSETSCOPE-000
 * [Semantic] Asset ownership scope.
 * 
 * <p>Defines the ownership boundary of an asset.</p>
 * 
 * @since 2.0.0
 */
public enum AssetScope {
    /**
     * Player scope (belongs to a specific player).
     */
    PLAYER,
    
    /**
     * Server scope (belongs to a server instance).
     */
    SERVER,
    
    /**
     * Global scope (belongs to the whole system).
     */
    GLOBAL,
    
    /**
     * Unknown scope (for backward compatibility).
     */
    UNKNOWN;
    
    /**
     * Parse a scope token.
     * 
     * @param str string token
     * @return scope; returns {@link #UNKNOWN} if parsing fails
     */
    public static AssetScope fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

