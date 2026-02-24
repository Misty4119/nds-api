package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * [Index] NDS-JAVA-EVENTHANDLER-000
 * [Semantic] Subscriber callback for NDS events.
 *
 * <p>[Constraint] Handlers must not mutate the event.</p>
 * <p>[Constraint] Handlers must not block the calling thread.</p>
 * <p>[Behavior] Exceptions thrown by a handler do not affect event persistence or other subscribers.</p>
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface NdsEventHandler {

    /** @param event immutable event to handle */
    void handle(NdsEvent event);
}

