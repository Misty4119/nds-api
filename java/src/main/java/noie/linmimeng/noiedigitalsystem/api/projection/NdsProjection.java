package noie.linmimeng.noiedigitalsystem.api.projection;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import java.util.List;

/**
 * [Index] NDS-JAVA-PROJECTION-000
 * [Semantic] Pure function that derives state from an ordered event stream.
 *
 * <p>[Constraint] Implementations must be pure (no side effects) and produce immutable results.</p>
 * <p>[Constraint] Must support deterministic replay.</p>
 *
 * @param <T> projected state type
 * @since 2.0.0
 */
public interface NdsProjection<T> {

    /** @return globally unique projection ID */
    ProjectionId id();

    /**
     * @param events chronologically ordered event list
     * @return projected state
     */
    T apply(List<NdsEvent> events);

    /**
     * Incremental update: apply a single event to the current state.
     *
     * <p>[Behavior] Default implementation recomputes from the full event set.
     * Implementations MAY override for incremental performance.</p>
     *
     * @param currentState current projected state; null = initial state
     * @param event new event to apply
     * @return updated projected state
     */
    default T apply(T currentState, NdsEvent event) {
        // [Index] NDS-JAVA-PROJECTION-010 [Behavior] Default implementation: recompute from the full event set.
        // [Index] NDS-JAVA-PROJECTION-011 [Behavior] Implementations MAY override for incremental updates.
        List<NdsEvent> allEvents = currentState != null
            ? List.of() // [Index] NDS-JAVA-PROJECTION-012 [Trace] Caller/runtime should provide the full event stream.
            : List.of(event);
        return apply(allEvents);
    }

    /** @return projection name for logging/diagnostics; defaults to id().value() */
    default String name() {
        return id().value();
    }
}

