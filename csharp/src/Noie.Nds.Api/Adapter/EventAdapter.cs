using Noie.Nds.Api.Event;
using Noie.Nds.Api.Identity;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-EVENTADAPTER-000] Event adapter (INdsEvent ↔ proto-compatible DTO).
/// 
/// <para>[Semantic] Bidirectional conversion between domain events and proto transport shapes.</para>
/// </summary>
public static class EventAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-010] Convert domain EventType to a proto string token.
    /// </summary>
    public static string ToProtoString(EventType type)
    {
        return type.ToProtoString();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-011] Convert a proto string token to domain EventType.
    /// </summary>
    public static EventType FromProtoString(string? protoType)
    {
        return EventTypeExtensions.FromString(protoType);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-020] Convert DateTimeOffset to Unix epoch milliseconds.
    /// </summary>
    public static long ToUnixMillis(DateTimeOffset dateTime)
    {
        return dateTime.ToUnixTimeMilliseconds();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-021] Convert Unix epoch milliseconds to DateTimeOffset.
    /// </summary>
    public static DateTimeOffset FromUnixMillis(long unixMillis)
    {
        return DateTimeOffset.FromUnixTimeMilliseconds(unixMillis);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-030] Create a proto-compatible event ID tuple.
    /// </summary>
    public static (string Value, long TimestampMillis) ToProtoData(IEventId eventId)
    {
        return (eventId.Value, eventId.Timestamp.ToUnixTimeMilliseconds());
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-031] Create a domain IEventId from a proto-compatible tuple.
    /// </summary>
    public static IEventId FromProtoData(string value, long timestampMillis)
    {
        return EventId.Of(value, DateTimeOffset.FromUnixTimeMilliseconds(timestampMillis));
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-040] Create a proto-compatible event DTO.
    /// </summary>
    public static EventProtoData ToProtoData(INdsEvent @event)
    {
        var (actorId, actorType, actorMeta) = IdentityAdapter.ToProtoData(@event.Actor);
        var (eventIdValue, eventIdTimestamp) = ToProtoData(@event.Id);
        
        return new EventProtoData(
            EventIdValue: eventIdValue,
            EventIdTimestamp: eventIdTimestamp,
            OccurredAtMillis: @event.OccurredAt.ToUnixTimeMilliseconds(),
            ActorId: actorId,
            ActorType: actorType,
            ActorMetadata: actorMeta,
            Type: @event.Type.ToProtoString(),
            PayloadData: @event.Payload.Data.ToDictionary(
                kvp => kvp.Key, 
                kvp => kvp.Value?.ToString() ?? ""),
            SchemaVersion: @event.SchemaVersion,
            Metadata: new Dictionary<string, string>(@event.Metadata)
        );
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTADAPTER-041] Create a domain event from a proto-compatible DTO.
    /// </summary>
    public static INdsEvent FromProtoData(EventProtoData data)
    {
        var eventId = FromProtoData(data.EventIdValue, data.EventIdTimestamp);
        var occurredAt = DateTimeOffset.FromUnixTimeMilliseconds(data.OccurredAtMillis);
        var actor = IdentityAdapter.FromProtoData(data.ActorId, data.ActorType, data.ActorMetadata);
        var type = EventTypeExtensions.FromString(data.Type);
        
        var payloadDict = data.PayloadData.ToDictionary(
            kvp => kvp.Key, 
            kvp => (object?)kvp.Value);
        var payload = NdsPayload.Of(payloadDict);
        
        return NdsEvent.Of(eventId, occurredAt, actor, type, payload, data.SchemaVersion, data.Metadata);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-EVENTADAPTER-100] Event proto transport DTO.
/// </summary>
public sealed record EventProtoData(
    string EventIdValue,
    long EventIdTimestamp,
    long OccurredAtMillis,
    string ActorId,
    string ActorType,
    Dictionary<string, string> ActorMetadata,
    string Type,
    Dictionary<string, string> PayloadData,
    int SchemaVersion,
    Dictionary<string, string> Metadata
);
