package noie.linmimeng.noiedigitalsystem.api.projection;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.context.NdsContext;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-QUERYSERVICE-000
 * [Semantic] Read-only query service for balances, event history, and projections.
 *
 * <p>[Constraint] All operations are async; implementations must not block the calling thread.</p>
 * <p>[Constraint] Results are derived from event projections; direct state mutation is prohibited.</p>
 *
 * @since 2.0.0
 */
public interface NdsQueryService {

    /**
     * @param asset target asset
     * @param identity owner identity; null = server-scoped asset
     * @param context tracing context (nullable)
     * @return async balance result
     */
    CompletableFuture<NdsResult<BigDecimal>> queryBalance(
        AssetId asset,
        NdsIdentity identity,
        NdsContext context
    );

    /** Convenience overload without context. */
    default CompletableFuture<NdsResult<BigDecimal>> queryBalance(
        AssetId asset,
        NdsIdentity identity
    ) {
        return queryBalance(asset, identity, null);
    }

    /**
     * @param asset filter by asset; null = all assets
     * @param identity filter by identity; null = all identities
     * @param startTime lower bound (inclusive, nullable)
     * @param endTime upper bound (exclusive, nullable)
     * @param limit maximum result count
     * @param offset pagination offset
     * @param context tracing context (nullable)
     * @return async event list result ordered by time
     */
    CompletableFuture<NdsResult<List<NdsEvent>>> queryHistory(
        AssetId asset,
        NdsIdentity identity,
        Instant startTime,
        Instant endTime,
        int limit,
        int offset,
        NdsContext context
    );

    /**
     * @param projectionId projection to query
     * @param context tracing context (nullable)
     * @return async projection state result
     */
    <T> CompletableFuture<NdsResult<T>> queryProjection(
        ProjectionId projectionId,
        NdsContext context
    );

    /** Convenience overload without context. */
    default <T> CompletableFuture<NdsResult<T>> queryProjection(ProjectionId projectionId) {
        return queryProjection(projectionId, null);
    }

    /**
     * Replay events up to {@code targetTime} and return the projected state.
     *
     * @param projectionId projection to replay
     * @param targetTime replay target timestamp
     * @param context tracing context (nullable)
     * @return async projected state result at targetTime
     */
    <T> CompletableFuture<NdsResult<T>> replay(
        ProjectionId projectionId,
        Instant targetTime,
        NdsContext context
    );

    /**
     * @param projection projection to register
     * @return async registration result
     */
    <T> CompletableFuture<NdsResult<Void>> registerProjection(NdsProjection<T> projection);

    /**
     * @param projectionId projection to unregister
     * @return async unregistration result
     */
    CompletableFuture<NdsResult<Void>> unregisterProjection(ProjectionId projectionId);
}

