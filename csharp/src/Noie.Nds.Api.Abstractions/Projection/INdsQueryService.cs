using Noie.Nds.Api.Asset;
using Noie.Nds.Api.Context;
using Noie.Nds.Api.Event;
using Noie.Nds.Api.Identity;
using Noie.Nds.Api.Result;

namespace Noie.Nds.Api.Projection;

/// <summary>
/// [Index: NDS-CSHARP-QUERY-000] NDS Query Service interface.
/// 
/// <para>[Semantic] Read-only query surface for projections and historical events.</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] All queries are asynchronous.</item>
///   <item>[Constraint] Results are derived from projections (event history), not mutable state.</item>
///   <item>[Constraint] Queries must not mutate state.</item>
/// </list>
/// </summary>
public interface INdsQueryService
{
    /// <summary>
    /// Query asset balance.
    /// </summary>
    /// <param name="asset">Asset ID.</param>
    /// <param name="identity">Identity (nullable; indicates server asset when null).</param>
    /// <param name="context">Context (optional).</param>
    /// <returns>Task containing the balance result.</returns>
    Task<NdsResult<decimal>> QueryBalanceAsync(
        IAssetId asset,
        INdsIdentity? identity,
        INdsContext? context = null);
    
    /// <summary>
    /// Query historical events.
    /// </summary>
    /// <param name="asset">Asset (nullable; all assets when null).</param>
    /// <param name="identity">Identity (nullable; all identities when null).</param>
    /// <param name="startTime">Start time (optional).</param>
    /// <param name="endTime">End time (optional).</param>
    /// <param name="limit">Limit.</param>
    /// <param name="offset">Offset.</param>
    /// <param name="context">Context (optional).</param>
    /// <returns>Task containing the event list result.</returns>
    Task<NdsResult<IReadOnlyList<INdsEvent>>> QueryHistoryAsync(
        IAssetId? asset,
        INdsIdentity? identity,
        DateTimeOffset? startTime,
        DateTimeOffset? endTime,
        int limit = 100,
        int offset = 0,
        INdsContext? context = null);
    
    /// <summary>
    /// Query a projection.
    /// </summary>
    /// <typeparam name="T">Projection result type.</typeparam>
    /// <param name="projectionId">Projection ID.</param>
    /// <param name="context">Context (optional).</param>
    /// <returns>Task containing the projection result.</returns>
    Task<NdsResult<T>> QueryProjectionAsync<T>(
        IProjectionId projectionId,
        INdsContext? context = null);
    
    /// <summary>
    /// Replay to a target time.
    /// </summary>
    /// <typeparam name="T">Projection result type.</typeparam>
    /// <param name="projectionId">Projection ID.</param>
    /// <param name="targetTime">Target time.</param>
    /// <param name="context">Context (optional).</param>
    /// <returns>Task containing the replayed state result.</returns>
    Task<NdsResult<T>> ReplayAsync<T>(
        IProjectionId projectionId,
        DateTimeOffset targetTime,
        INdsContext? context = null);
    
    /// <summary>
    /// Register a projection.
    /// </summary>
    /// <typeparam name="T">Projection result type.</typeparam>
    /// <param name="projection">Projection instance.</param>
    /// <returns>Task containing the registration result.</returns>
    Task<NdsResult<Unit>> RegisterProjectionAsync<T>(INdsProjection<T> projection);
    
    /// <summary>
    /// Unregister a projection.
    /// </summary>
    /// <param name="projectionId">Projection ID.</param>
    /// <returns>Task containing the unregistration result.</returns>
    Task<NdsResult<Unit>> UnregisterProjectionAsync(IProjectionId projectionId);
}
