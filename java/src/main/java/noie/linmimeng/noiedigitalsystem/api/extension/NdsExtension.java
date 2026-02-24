package noie.linmimeng.noiedigitalsystem.api.extension;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;

/**
 * [Index] NDS-JAVA-EXTENSION-000
 * [Semantic] Safe extension slot allowing plugins to react to events and emit new events.
 *
 * <p>[Constraint] Extensions must not mutate the input event.</p>
 * <p>[Constraint] Extensions must not block the calling thread.</p>
 * <p>[Constraint] Extensions may only produce new events; they must not perform direct state mutations.</p>
 *
 * @since 2.0.0
 */
public interface NdsExtension {

    /**
     * @return unique namespace identifying this extension (e.g. "myplugin", "shop")
     */
    String namespace();

    /**
     * React to an event and optionally emit new events.
     *
     * @param event immutable input event
     * @return result containing a list of new events to publish; empty list if none
     */
    NdsResult<List<NdsEvent>> onEvent(NdsEvent event);

    /**
     * @return execution priority; lower value = higher priority (default: 100)
     */
    default int priority() {
        return 100;
    }

    /** @return true if this extension is active */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Called when the extension is registered.
     * Override to initialize resources (caches, connections, configuration).
     */
    default void onEnable() {
        // [Index] NDS-JAVA-EXTENSION-001 [Behavior] Default no-op implementation.
    }

    /**
     * Called when the extension is unregistered or the runtime shuts down.
     * Override to release resources (connections, caches, persisted state).
     */
    default void onDisable() {
        // [Index] NDS-JAVA-EXTENSION-002 [Behavior] Default no-op implementation.
    }
}

