namespace Noie.Nds.Api.Asset;

/// <summary>
/// [Index: NDS-CSHARP-ASSETID-000] Asset ID.
/// 
/// <para>[Semantic] Globally unique identifier for an asset.</para>
/// </summary>
public interface IAssetId
{
    /// <summary>
    /// Get the asset name.
    /// </summary>
    /// <returns>Asset name (e.g. "coins", "gold", "world_boss_hp").</returns>
    string Name { get; }
    
    /// <summary>
    /// Get the canonical full ID string.
    /// </summary>
    /// <returns>Full ID in the form "scope:name" (e.g. "player:coins", "server:world_boss_hp").</returns>
    string FullId { get; }
    
    /// <summary>
    /// Get the asset scope.
    /// </summary>
    AssetScope Scope { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-ASSETID-100] AssetId factory.
/// </summary>
public static class AssetId
{
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETID-110] Parse an AssetId from its canonical string form.
    /// </summary>
    /// <param name="fullId">Full ID string.</param>
    /// <returns>AssetId instance.</returns>
    /// <exception cref="ArgumentException">If the format is invalid.</exception>
    public static IAssetId FromString(string fullId)
    {
        if (string.IsNullOrEmpty(fullId))
        {
            throw new ArgumentException("AssetId cannot be null or empty", nameof(fullId));
        }
        
        var colonIndex = fullId.IndexOf(':');
        if (colonIndex <= 0 || colonIndex >= fullId.Length - 1)
        {
            throw new ArgumentException($"Invalid AssetId format: {fullId}", nameof(fullId));
        }
        
        var scopeStr = fullId[..colonIndex];
        var name = fullId[(colonIndex + 1)..];
        var scope = AssetScopeExtensions.FromString(scopeStr);
        
        return Of(scope, name);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETID-120] Create an AssetId.
    /// </summary>
    /// <param name="scope">Scope.</param>
    /// <param name="name">Name.</param>
    /// <returns>AssetId instance.</returns>
    public static IAssetId Of(AssetScope scope, string name)
    {
        return new AssetIdImpl(scope, name);
    }
    
    /// <summary>
    /// Create a player-scoped AssetId.
    /// </summary>
    public static IAssetId Player(string name) => Of(AssetScope.Player, name);
    
    /// <summary>
    /// Create a server-scoped AssetId.
    /// </summary>
    public static IAssetId Server(string name) => Of(AssetScope.Server, name);
    
    /// <summary>
    /// Create a global-scoped AssetId.
    /// </summary>
    public static IAssetId Global(string name) => Of(AssetScope.Global, name);
}

/// <summary>
/// [Index: NDS-CSHARP-ASSETID-900] AssetId internal implementation.
/// </summary>
internal sealed record AssetIdImpl(AssetScope Scope, string Name) : IAssetId
{
    public string FullId => $"{Scope.ToProtoString().ToLowerInvariant()}:{Name}";
    
    public override string ToString() => FullId;
}
