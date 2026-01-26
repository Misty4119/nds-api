using Noie.Nds.Api.Asset;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-ASSETADAPTER-000] Asset adapter (IAssetId ↔ proto-compatible shapes).
/// 
/// <para>[Semantic] Conversion utilities for asset scopes and canonical ID formats.</para>
/// </summary>
public static class AssetAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-010] Convert domain AssetScope to a proto string token.
    /// </summary>
    public static string ToProtoString(AssetScope scope)
    {
        return scope.ToProtoString();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-011] Convert a proto string token to domain AssetScope.
    /// </summary>
    public static AssetScope FromProtoString(string? protoScope)
    {
        return AssetScopeExtensions.FromString(protoScope);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-020] Serialize a domain asset ID to canonical string form.
    /// </summary>
    /// <param name="assetId">Domain asset ID.</param>
    /// <returns>Canonical full ID string.</returns>
    public static string Serialize(IAssetId assetId)
    {
        return assetId.FullId;
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-021] Deserialize canonical string form into a domain asset ID.
    /// </summary>
    /// <param name="fullId">Full ID string.</param>
    /// <returns>Domain asset ID.</returns>
    public static IAssetId Deserialize(string fullId)
    {
        return Asset.AssetId.FromString(fullId);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-030] Create a proto-compatible asset ID tuple.
    /// </summary>
    public static (string Name, string Scope) ToProtoData(IAssetId assetId)
    {
        return (assetId.Name, assetId.Scope.ToProtoString());
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-ASSETADAPTER-031] Create a domain asset ID from proto-compatible data.
    /// </summary>
    public static IAssetId FromProtoData(string name, string scope)
    {
        var assetScope = AssetScopeExtensions.FromString(scope);
        return Asset.AssetId.Of(assetScope, name);
    }
}
