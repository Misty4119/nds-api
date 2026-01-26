namespace Noie.Nds.Api.Event;

/// <summary>
/// [Index: NDS-CSHARP-EVENTTYPE-000] Event type.
/// 
/// <para>[Semantic] Standard event type taxonomy.</para>
/// </summary>
public enum EventType
{
    /// <summary>
    /// Transaction event (asset delta semantics; see INdsTransaction).
    /// </summary>
    Transaction,
    
    /// <summary>
    /// Asset created.
    /// </summary>
    AssetCreated,
    
    /// <summary>
    /// Asset updated.
    /// </summary>
    AssetUpdated,
    
    /// <summary>
    /// Asset deleted.
    /// </summary>
    AssetDeleted,
    
    /// <summary>
    /// Identity created.
    /// </summary>
    IdentityCreated,
    
    /// <summary>
    /// Identity updated.
    /// </summary>
    IdentityUpdated,
    
    /// <summary>
    /// System event.
    /// </summary>
    System,
    
    /// <summary>
    /// Custom event (extension point).
    /// </summary>
    Custom
}

/// <summary>
/// [Index: NDS-CSHARP-EVENTTYPE-100] EventType extensions.
/// </summary>
public static class EventTypeExtensions
{
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTTYPE-110] Parse an event type token.
    /// </summary>
    public static EventType FromString(string? str)
    {
        if (string.IsNullOrEmpty(str)) return EventType.Custom;
        
        return str.ToUpperInvariant() switch
        {
            "TRANSACTION" => EventType.Transaction,
            "ASSET_CREATED" => EventType.AssetCreated,
            "ASSET_UPDATED" => EventType.AssetUpdated,
            "ASSET_DELETED" => EventType.AssetDeleted,
            "IDENTITY_CREATED" => EventType.IdentityCreated,
            "IDENTITY_UPDATED" => EventType.IdentityUpdated,
            "SYSTEM" => EventType.System,
            "CUSTOM" => EventType.Custom,
            _ => EventType.Custom
        };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-EVENTTYPE-120] Convert to proto string token.
    /// </summary>
    public static string ToProtoString(this EventType type)
    {
        return type switch
        {
            EventType.Transaction => "TRANSACTION",
            EventType.AssetCreated => "ASSET_CREATED",
            EventType.AssetUpdated => "ASSET_UPDATED",
            EventType.AssetDeleted => "ASSET_DELETED",
            EventType.IdentityCreated => "IDENTITY_CREATED",
            EventType.IdentityUpdated => "IDENTITY_UPDATED",
            EventType.System => "SYSTEM",
            EventType.Custom => "CUSTOM",
            _ => "CUSTOM"
        };
    }
}
