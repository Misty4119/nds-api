package noie.linmimeng.noiedigitalsystem.api.identity;

import java.util.Map;

/**
 * [Index] NDS-JAVA-IDENTITY-000
 * [Semantic] Identity contract for actors (player/system/AI/external).
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *   <li>[Constraint] Must be immutable.</li>
 *   <li>[Constraint] {@link #id()} must be globally unique within its {@link #type()}.</li>
 * </ul>
 *
 * @since 2.0.0
 */
public interface NdsIdentity {
    
    /**
     * Get the identity ID.
     *
     * @return globally unique identity ID (format depends on type)
     */
    String id();
    
    /**
     * Get the identity type.
     *
     * @return identity type
     */
    IdentityType type();
    
    /**
     * Get identity metadata.
     *
     * @return metadata map (may be empty; never null)
     */
    Map<String, String> metadata();
    
    /**
     * Basic structural validation (null/empty checks only).
     *
     * @return true if structurally valid
     */
    default boolean isValid() {
        return id() != null && !id().isEmpty() && type() != null;
    }
    
    /**
     * Create a copy with new metadata (immutability helper).
     *
     * @param metadata new metadata (may be null)
     * @return new identity instance
     */
    NdsIdentity withMetadata(Map<String, String> metadata);
    
    /**
     * Parse from a string.
     *
     * <p>Supported forms:</p>
     * <ul>
     *   <li>{@code type:id} (e.g. {@code PLAYER:550e8400-e29b-41d4-a716-446655440000})</li>
     *   <li>{@code id} (defaults to {@link IdentityType#PLAYER})</li>
     * </ul>
     *
     * @param rawId raw identity string
     * @return identity instance
     * @throws IllegalArgumentException if format is invalid
     */
    static NdsIdentity fromString(String rawId) {
        if (rawId == null || rawId.isEmpty()) {
            throw new IllegalArgumentException("Identity ID cannot be null or empty");
        }
        if (rawId.contains(":")) {
            String[] parts = rawId.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid identity format: " + rawId);
            }
            IdentityType type = IdentityType.fromString(parts[0]);
            return of(parts[1], type);
        }
        // [Index] NDS-JAVA-IDENTITY-010 [Behavior] If no type prefix is provided, default to PLAYER.
        return of(rawId, IdentityType.PLAYER);
    }
    
    /**
     * Create an identity (lightweight; no IO).
     *
     * @param id identity id
     * @param type identity type
     * @return identity instance
     */
    static NdsIdentity of(String id, IdentityType type) {
        return new NdsIdentityImpl(id, type, Map.of()); // [Index] NDS-JAVA-IDENTITY-011 [Behavior] Lightweight value object creation (no IO).
    }
}

