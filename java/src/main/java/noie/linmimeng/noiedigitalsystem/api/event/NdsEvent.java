package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.payload.NdsPayload;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * [Index] NDS-JAVA-EVENT-000
 * [Semantic] Immutable domain event — the authoritative record of all state changes.
 *
 * <p>[Constraint] Implementations must be immutable and fully serializable.</p>
 * <p>[Constraint] Payload must not carry platform-specific or non-serializable types.</p>
 * <p>[Constraint] Events must support deterministic replay.</p>
 *
 * @since 2.0.0
 */
public interface NdsEvent {

    /** @return globally unique event ID */
    EventId id();

    /** @return UTC timestamp when the event occurred */
    Instant occurredAt();

    /** @return initiating identity (may be a system identity) */
    NdsIdentity actor();

    /** @return event type */
    EventType type();

    /** @return serializable event payload */
    NdsPayload payload();

    /**
     * @return payload schema version; used for forward/backward compatibility (default: 1)
     */
    default int schemaVersion() {
        return 1;
    }

    /** @return supplementary metadata (never null; may be empty) */
    default Map<String, String> metadata() {
        return Collections.emptyMap();
    }

    /** @return true if all required fields are non-null and actor is valid */
    default boolean isValid() {
        return id() != null
            && occurredAt() != null
            && actor() != null
            && actor().isValid()
            && type() != null
            && payload() != null;
    }
}

