namespace Noie.Nds.Api.Event;

/// <summary>
/// [Index: NDS-CSHARP-EVENTID-000] Event ID.
/// 
/// <para>[Semantic] Globally unique identifier for an event.</para>
/// </summary>
public interface IEventId
{
    /// <summary>
    /// Get the event ID value.
    /// </summary>
    string Value { get; }
    
    /// <summary>
    /// Get the event timestamp.
    /// </summary>
    DateTimeOffset Timestamp { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-EVENTID-100] EventId factory.
/// </summary>
public static class EventId
{
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTID-110] Generate a new event ID.
    /// </summary>
    public static IEventId Generate()
    {
        return new EventIdImpl(Guid.NewGuid().ToString(), DateTimeOffset.UtcNow);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTID-120] Create an event ID from a string value (timestamp=now).
    /// </summary>
    public static IEventId FromString(string value)
    {
        return new EventIdImpl(value, DateTimeOffset.UtcNow);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTID-121] Create an event ID with an explicit timestamp.
    /// </summary>
    public static IEventId Of(string value, DateTimeOffset timestamp)
    {
        return new EventIdImpl(value, timestamp);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-EVENTID-900] EventId internal implementation.
/// </summary>
internal sealed record EventIdImpl(string Value, DateTimeOffset Timestamp) : IEventId
{
    public override string ToString() => Value;
}
