package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * [Index] NDS-JAVA-EVENTID-000
 * [Semantic] Globally unique event identifier.
 *
 * @since 2.0.0
 */
public interface EventId {

    /** @return ID string (UUID or timestamp-based) */
    String value();

    /** @return new EventId backed by a random UUID */
    static EventId generate() {
        return new EventIdImpl(java.util.UUID.randomUUID().toString());
    }

    /**
     * @param value non-empty ID string
     * @return EventId wrapping the given value
     * @throws IllegalArgumentException if value is null or empty
     */
    static EventId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("EventId cannot be null or empty");
        }
        return new EventIdImpl(value);
    }
}

