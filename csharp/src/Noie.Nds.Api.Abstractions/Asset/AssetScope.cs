namespace Noie.Nds.Api.Asset;

/// <summary>
/// [Index: NDS-CSHARP-ASSETSCOPE-000] Asset scope.
/// 
/// <para>[Semantic] Defines the ownership boundary of an asset.</para>
/// </summary>
public enum AssetScope
{
    /// <summary>
    /// Player scope (belongs to a specific player).
    /// </summary>
    Player,
    
    /// <summary>
    /// Server scope (belongs to a server instance).
    /// </summary>
    Server,
    
    /// <summary>
    /// Global scope (belongs to the whole system).
    /// </summary>
    Global,
    
    /// <summary>
    /// Unknown scope (for backward compatibility).
    /// </summary>
    Unknown
}

/// <summary>
/// [Index: NDS-CSHARP-ASSETSCOPE-100] AssetScope extensions.
/// </summary>
public static class AssetScopeExtensions
{
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETSCOPE-110] Parse a scope token.
    /// </summary>
    /// <param name="str">String token.</param>
    /// <returns>Scope; returns Unknown if parsing fails.</returns>
    public static AssetScope FromString(string? str)
    {
        if (string.IsNullOrEmpty(str)) return AssetScope.Unknown;
        
        return str.ToUpperInvariant() switch
        {
            "PLAYER" => AssetScope.Player,
            "SERVER" => AssetScope.Server,
            "GLOBAL" => AssetScope.Global,
            _ => AssetScope.Unknown
        };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETSCOPE-120] Convert to proto string token.
    /// </summary>
    public static string ToProtoString(this AssetScope scope)
    {
        return scope switch
        {
            AssetScope.Player => "PLAYER",
            AssetScope.Server => "SERVER",
            AssetScope.Global => "GLOBAL",
            _ => "UNKNOWN"
        };
    }
}
