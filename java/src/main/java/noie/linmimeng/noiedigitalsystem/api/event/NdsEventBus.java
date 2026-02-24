package noie.linmimeng.noiedigitalsystem.api.event;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-EVENTBUS-000
 * [Semantic] Event publication and subscription contract.
 *
 * @since 2.0.0
 */
public interface NdsEventBus {

    /**
     * Publish an event.
     *
     * <p>[Behavior] Future completion means the event is durably persisted to the event store —
     * NOT merely enqueued in memory and NOT that all subscribers have finished processing.</p>
     *
     * @param event immutable event to publish (must satisfy {@link NdsEvent#isValid()})
     * @return async result; completes when persistence succeeds
     */
    CompletableFuture<NdsResult<Void>> publish(NdsEvent event);

    /**
     * @param eventType event type to subscribe to
     * @param handler event handler
     * @return subscription ID (use to unsubscribe)
     */
    String subscribe(EventType eventType, NdsEventHandler handler);

    /**
     * Subscribe to all event types.
     *
     * @param handler event handler
     * @return subscription ID
     */
    String subscribeAll(NdsEventHandler handler);

    /**
     * @param subscriptionId subscription ID returned by {@link #subscribe} or {@link #subscribeAll}
     */
    void unsubscribe(String subscriptionId);

    /**
     * @param eventType filter by type; null = all types
     * @param startTime lower bound (inclusive, nullable)
     * @param endTime upper bound (exclusive, nullable)
     * @param limit maximum number of results
     * @param offset pagination offset
     * @return async result containing matched events ordered by time
     */
    CompletableFuture<NdsResult<List<NdsEvent>>> queryHistory(
        EventType eventType,
        Instant startTime,
        Instant endTime,
        int limit,
        int offset
    );
}

