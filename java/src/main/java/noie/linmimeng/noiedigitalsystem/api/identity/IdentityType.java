package noie.linmimeng.noiedigitalsystem.api.identity;

/**
 * [Index] NDS-JAVA-IDENTITYTYPE-000
 * [Semantic] Identity type.
 * 
 * @since 2.0.0
 */
public enum IdentityType {
    /**
     * Player identity.
     * ID format: UUID (e.g. "550e8400-e29b-41d4-a716-446655440000").
     */
    PLAYER,
    
    /**
     * System identity.
     * ID format: system-defined string (e.g. "system:admin", "system:event").
     */
    SYSTEM,
    
    /**
     * AI identity.
     * ID format: AI service-defined string (e.g. "ai:gpt-4", "ai:claude").
     */
    AI,
    
    /**
     * External service identity.
     * ID format: external service-defined string (e.g. "external:payment:stripe").
     */
    EXTERNAL,
    
    /**
     * Unknown type (for backward compatibility).
     */
    UNKNOWN;
    
    /**
     * Parse an identity type token.
     * 
     * @param str string token
     * @return type; returns {@link #UNKNOWN} if parsing fails
     */
    public static IdentityType fromString(String str) {
        if (str == null) return UNKNOWN;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

