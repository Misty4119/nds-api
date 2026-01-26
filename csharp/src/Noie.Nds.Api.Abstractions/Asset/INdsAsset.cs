namespace Noie.Nds.Api.Asset;

/// <summary>
/// [Index: NDS-CSHARP-NDSASSET-000] NDS asset.
/// 
/// <para>[Semantic] Asset definition containing ID, current value, and optional limit.</para>
/// </summary>
public interface INdsAsset
{
    /// <summary>
    /// Get the asset ID.
    /// </summary>
    IAssetId Id { get; }
    
    /// <summary>
    /// Get the asset display name.
    /// </summary>
    string DisplayName { get; }
    
    /// <summary>
    /// Get the current value.
    /// </summary>
    decimal Value { get; }
    
    /// <summary>
    /// Get the limit (nullable; null means unlimited).
    /// </summary>
    decimal? Limit { get; }
    
    /// <summary>
    /// Get the floor (default=0).
    /// </summary>
    decimal Floor => 0m;
    
    /// <summary>
    /// Get asset metadata.
    /// </summary>
    IReadOnlyDictionary<string, string> Metadata { get; }
    
    /// <summary>
    /// Check whether the asset can add the given amount.
    /// </summary>
    bool CanAdd(decimal amount)
    {
        if (amount < 0) return false;
        if (Limit == null) return true;
        return Value + amount <= Limit.Value;
    }
    
    /// <summary>
    /// Check whether the asset can subtract the given amount.
    /// </summary>
    bool CanSubtract(decimal amount)
    {
        if (amount < 0) return false;
        return Value - amount >= Floor;
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSASSET-100] NdsAsset factory.
/// </summary>
public static class NdsAsset
{
    /// <summary>
    /// Create an asset.
    /// </summary>
    public static INdsAsset Of(
        IAssetId id,
        string displayName,
        decimal value,
        decimal? limit = null,
        IReadOnlyDictionary<string, string>? metadata = null)
    {
        return new NdsAssetImpl(id, displayName, value, limit, 
            metadata ?? new Dictionary<string, string>());
    }
    
    /// <summary>
    /// Create a player-scoped asset.
    /// </summary>
    public static INdsAsset Player(string name, decimal value, decimal? limit = null)
    {
        return Of(AssetId.Player(name), name, value, limit);
    }
    
    /// <summary>
    /// Create a server-scoped asset.
    /// </summary>
    public static INdsAsset Server(string name, decimal value, decimal? limit = null)
    {
        return Of(AssetId.Server(name), name, value, limit);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSASSET-900] NdsAsset internal implementation.
/// </summary>
internal sealed record NdsAssetImpl(
    IAssetId Id,
    string DisplayName,
    decimal Value,
    decimal? Limit,
    IReadOnlyDictionary<string, string> Metadata
) : INdsAsset;
