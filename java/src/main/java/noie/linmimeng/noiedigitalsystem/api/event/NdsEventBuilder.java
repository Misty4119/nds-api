package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.payload.NdsPayload;
import java.time.Instant;
import java.util.Map;

/**
 * [Index] NDS-JAVA-EVENTBUILDER-000
 * [Semantic] Builder for immutable {@link NdsEvent} instances.
 *
 * @since 2.0.0
 */
public interface NdsEventBuilder {

    /**
     * @param id event ID; auto-generated if null
     * @return this builder
     */
    NdsEventBuilder id(EventId id);

    /**
     * @param occurredAt event timestamp; defaults to current time if null
     * @return this builder
     */
    NdsEventBuilder occurredAt(Instant occurredAt);

    /**
     * @param actor initiating identity (required)
     * @return this builder
     */
    NdsEventBuilder actor(NdsIdentity actor);

    /**
     * @param type event type (required)
     * @return this builder
     */
    NdsEventBuilder type(EventType type);

    /**
     * @param payload serializable event payload (required)
     * @return this builder
     */
    NdsEventBuilder payload(NdsPayload payload);

    /**
     * @param schemaVersion payload schema version (default: 1)
     * @return this builder
     */
    NdsEventBuilder schemaVersion(int schemaVersion);

    /**
     * @param metadata supplementary metadata (nullable)
     * @return this builder
     */
    NdsEventBuilder metadata(Map<String, String> metadata);

    /**
     * Build the immutable event.
     *
     * <p>[Constraint] All payload values are validated at build time against the allowed type set.
     * Prohibited types (e.g. platform-specific objects) throw {@link IllegalArgumentException} immediately.</p>
     * <p>[Constraint] When replaying historical events, supply the original ID and timestamp explicitly
     * via {@link #id(EventId)} and {@link #occurredAt(Instant)}.</p>
     *
     * <pre>{@code
     * // [Index] NDS-JAVA-EVENTBUILDER-EX-001 [Behavior] New event: auto-generate ID.
     * NdsEvent event = NdsEventBuilder.create()
     *     .actor(identity)
     *     .type(EventType.TRANSACTION)
     *     .payload(payload)
     *     .build();
     *
     * // [Index] NDS-JAVA-EVENTBUILDER-EX-003 [Constraint] Replay: preserve original ID/time.
     * NdsEvent replayedEvent = NdsEventBuilder.create()
     *     .id(originalEvent.id())
     *     .occurredAt(originalEvent.occurredAt())
     *     .actor(originalEvent.actor())
     *     .type(originalEvent.type())
     *     .payload(originalEvent.payload())
     *     .build();
     * }</pre>
     *
     * @return immutable NdsEvent
     * @throws IllegalStateException if required fields are missing
     * @throws IllegalArgumentException if payload contains prohibited types
     */
    NdsEvent build();

    /** @return new NdsEventBuilder (implementation provided by nds-core) */
    static NdsEventBuilder create() {
        // [Index] NDS-JAVA-EVENTBUILDER-900 [Trace] Implementation is provided by nds-core.
        throw new UnsupportedOperationException("NdsEventBuilder implementation must be provided by nds-core");
    }
}

