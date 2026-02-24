package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * [Index] NDS-JAVA-EVENTTYPE-000
 * [Semantic] Standard event type taxonomy.
 *
 * @since 2.0.0
 */
public enum EventType {
    /** Asset delta event (see NdsTransaction). */
    TRANSACTION,

    /** Asset creation event. */
    ASSET_CREATED,

    /** Asset metadata update event. */
    ASSET_UPDATED,

    /** Asset deletion event. */
    ASSET_DELETED,

    /** Identity creation event. */
    IDENTITY_CREATED,

    /** Identity metadata update event. */
    IDENTITY_UPDATED,

    /** System-level event. */
    SYSTEM,

    /** Custom / extension event. */
    CUSTOM;

    /**
     * @param str string token (case-insensitive)
     * @return parsed type; defaults to {@link #CUSTOM} if null or unrecognized
     */
    public static EventType fromString(String str) {
        if (str == null) return CUSTOM;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CUSTOM;
        }
    }
}

