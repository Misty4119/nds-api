using Noie.Nds.Api.Event;

namespace Noie.Nds.Api.Projection;

/// <summary>
/// [Index: NDS-CSHARP-PROJECTION-000] NDS Projection interface.
/// 
/// <para>[Semantic] Pure function that derives state from an event list.</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] Must be pure (no side effects).</item>
///   <item>[Behavior] Must support replay.</item>
///   <item>[Constraint] Result must be immutable.</item>
/// </list>
/// </summary>
/// <typeparam name="T">Projection result type.</typeparam>
public interface INdsProjection<T>
{
    /// <summary>
    /// Get the projection ID.
    /// </summary>
    /// <returns>Projection ID (globally unique).</returns>
    IProjectionId Id { get; }
    
    /// <summary>
    /// Get the projection name.
    /// </summary>
    /// <returns>Projection name (for logs/debug).</returns>
    string Name => Id.Value;
    
    /// <summary>
    /// Apply an ordered event list and derive state.
    /// 
    /// <para>[Constraint] Must be side-effect free.</para>
    /// </summary>
    /// <param name="events">Events in chronological order.</param>
    /// <returns>Projection result.</returns>
    T Apply(IReadOnlyList<INdsEvent> events);
    
    /// <summary>
    /// Apply a single event and update state.
    /// 
    /// <para>[Behavior] Intended for incremental updates.</para>
    /// </summary>
    /// <param name="currentState">Current state (nullable; indicates initial state).</param>
    /// <param name="event">New event.</param>
    /// <returns>Updated state.</returns>
    T Apply(T? currentState, INdsEvent @event);
    
    /// <summary>
    /// Get the initial state.
    /// </summary>
    T InitialState { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-PROJECTION-100] Projection status.
/// </summary>
public enum ProjectionStatus
{
    /// <summary>
    /// Active.
    /// </summary>
    Active,
    
    /// <summary>
    /// Rebuilding.
    /// </summary>
    Rebuilding,
    
    /// <summary>
    /// Paused.
    /// </summary>
    Paused,
    
    /// <summary>
    /// Error.
    /// </summary>
    Error
}
