using Noie.Nds.Api.Identity;

namespace Noie.Nds.Api.Event;

/// <summary>
/// [Index: NDS-CSHARP-NDSEVENT-000] NDS Event (immutable record).
/// 
/// <para>[Semantic] Immutable historical record of a state change (event sourcing).</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] Must be immutable.</item>
///   <item>[Constraint] Must not embed platform-specific / non-serializable objects.</item>
///   <item>[Constraint] Must be serializable.</item>
///   <item>[Behavior] Must support replay.</item>
/// </list>
/// </summary>
public interface INdsEvent
{
    /// <summary>
    /// Get the event ID.
    /// </summary>
    /// <returns>Globally unique event ID.</returns>
    IEventId Id { get; }
    
    /// <summary>
    /// Get the occurrence time.
    /// </summary>
    /// <returns>Occurrence time (UTC).</returns>
    DateTimeOffset OccurredAt { get; }
    
    /// <summary>
    /// Get the actor identity.
    /// </summary>
    /// <returns>Actor identity.</returns>
    INdsIdentity Actor { get; }
    
    /// <summary>
    /// Get the event type.
    /// </summary>
    EventType Type { get; }
    
    /// <summary>
    /// Get the event payload.
    /// </summary>
    /// <returns>Event payload.</returns>
    NdsPayload Payload { get; }
    
    /// <summary>
    /// Get the schema version.
    /// 
    /// <para>Used for backward compatibility and migrations.</para>
    /// </summary>
    /// <returns>Schema version (default=1).</returns>
    int SchemaVersion => 1;
    
    /// <summary>
    /// Get event metadata.
    /// </summary>
    /// <returns>Metadata map (may be empty; never null).</returns>
    IReadOnlyDictionary<string, string> Metadata => new Dictionary<string, string>();
    
    /// <summary>
    /// Check whether the event is valid.
    /// </summary>
    bool IsValid => Id != null && Actor != null && Actor.IsValid && Payload != null;
}

/// <summary>
/// [Index: NDS-CSHARP-NDSEVENT-100] NdsEvent factory.
/// </summary>
public static class NdsEvent
{
    /// <summary>
    /// Create an event builder.
    /// </summary>
    public static NdsEventBuilder Builder() => new();
    
    /// <summary>
    /// Create an event.
    /// </summary>
    public static INdsEvent Of(
        IEventId id,
        DateTimeOffset occurredAt,
        INdsIdentity actor,
        EventType type,
        NdsPayload payload,
        int schemaVersion = 1,
        IReadOnlyDictionary<string, string>? metadata = null)
    {
        return new NdsEventImpl(id, occurredAt, actor, type, payload, schemaVersion,
            metadata ?? new Dictionary<string, string>());
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSEVENT-200] NdsEvent builder.
/// </summary>
public sealed class NdsEventBuilder
{
    private IEventId? _id;
    private DateTimeOffset _occurredAt = DateTimeOffset.UtcNow;
    private INdsIdentity? _actor;
    private EventType _type = EventType.Custom;
    private NdsPayload _payload = NdsPayload.Empty();
    private int _schemaVersion = 1;
    private readonly Dictionary<string, string> _metadata = new();
    
    public NdsEventBuilder Id(IEventId id)
    {
        _id = id;
        return this;
    }
    
    public NdsEventBuilder OccurredAt(DateTimeOffset occurredAt)
    {
        _occurredAt = occurredAt;
        return this;
    }
    
    public NdsEventBuilder Actor(INdsIdentity actor)
    {
        _actor = actor;
        return this;
    }
    
    public NdsEventBuilder Type(EventType type)
    {
        _type = type;
        return this;
    }
    
    public NdsEventBuilder Payload(NdsPayload payload)
    {
        _payload = payload;
        return this;
    }
    
    public NdsEventBuilder SchemaVersion(int version)
    {
        _schemaVersion = version;
        return this;
    }
    
    public NdsEventBuilder WithMetadata(string key, string value)
    {
        _metadata[key] = value;
        return this;
    }
    
    public INdsEvent Build()
    {
        if (_id == null) _id = EventId.Generate();
        if (_actor == null) throw new InvalidOperationException("Actor is required");
        
        return new NdsEventImpl(_id, _occurredAt, _actor, _type, _payload, _schemaVersion, _metadata);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSEVENT-900] NdsEvent internal implementation.
/// </summary>
internal sealed record NdsEventImpl(
    IEventId Id,
    DateTimeOffset OccurredAt,
    INdsIdentity Actor,
    EventType Type,
    NdsPayload Payload,
    int SchemaVersion,
    IReadOnlyDictionary<string, string> Metadata
) : INdsEvent;
