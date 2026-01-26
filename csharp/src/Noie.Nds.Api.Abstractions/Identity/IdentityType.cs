namespace Noie.Nds.Api.Identity;

/// <summary>
/// [Index: NDS-CSHARP-IDENTITYTYPE-000] Identity type.
/// </summary>
public enum IdentityType
{
    /// <summary>
    /// Player identity.
    /// Recommended ID format: UUID (e.g. "550e8400-e29b-41d4-a716-446655440000").
    /// </summary>
    Player,
    
    /// <summary>
    /// System identity.
    /// Recommended ID format: system-defined string (e.g. "system:admin", "system:event").
    /// </summary>
    System,
    
    /// <summary>
    /// AI identity.
    /// Recommended ID format: AI-service-defined string (e.g. "ai:gpt-4", "ai:claude").
    /// </summary>
    Ai,
    
    /// <summary>
    /// External service identity.
    /// Recommended ID format: service-defined string (e.g. "external:payment:stripe").
    /// </summary>
    External,
    
    /// <summary>
    /// Unknown type (for backward compatibility).
    /// </summary>
    Unknown
}

/// <summary>
/// [Index: NDS-CSHARP-IDENTITYTYPE-100] IdentityType extensions.
/// </summary>
public static class IdentityTypeExtensions
{
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYTYPE-110] Parse an identity type token.
    /// </summary>
    /// <param name="str">String token.</param>
    /// <returns>Identity type; returns Unknown if parsing fails.</returns>
    public static IdentityType FromString(string? str)
    {
        if (string.IsNullOrEmpty(str)) return IdentityType.Unknown;
        
        return str.ToUpperInvariant() switch
        {
            "PLAYER" => IdentityType.Player,
            "SYSTEM" => IdentityType.System,
            "AI" => IdentityType.Ai,
            "EXTERNAL" => IdentityType.External,
            _ => IdentityType.Unknown
        };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYTYPE-120] Convert to proto string token.
    /// </summary>
    public static string ToProtoString(this IdentityType type)
    {
        return type switch
        {
            IdentityType.Player => "PLAYER",
            IdentityType.System => "SYSTEM",
            IdentityType.Ai => "AI",
            IdentityType.External => "EXTERNAL",
            _ => "UNKNOWN"
        };
    }
}
