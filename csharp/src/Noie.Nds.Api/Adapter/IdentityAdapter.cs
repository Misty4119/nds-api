using Noie.Nds.Api.Identity;
using System.Linq;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-IDENTITYADAPTER-000] Identity adapter (INdsIdentity ↔ proto-compatible shapes).
/// 
/// <para>[Semantic] Conversion utilities for identity types and canonical string formats.</para>
/// </summary>
public static class IdentityAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-010] Convert domain IdentityType to a proto string token.
    /// </summary>
    public static string ToProtoString(IdentityType type)
    {
        return type.ToProtoString();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-011] Convert a proto string token to domain IdentityType.
    /// </summary>
    public static IdentityType FromProtoString(string? protoType)
    {
        return IdentityTypeExtensions.FromString(protoType);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-020] Serialize a domain identity to canonical string form.
    /// </summary>
    /// <param name="identity">Domain identity.</param>
    /// <returns>Canonical string: "TYPE:id".</returns>
    public static string Serialize(INdsIdentity identity)
    {
        return $"{identity.Type.ToProtoString()}:{identity.Id}";
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-021] Deserialize canonical string form into a domain identity.
    /// </summary>
    /// <param name="serialized">Serialized string.</param>
    /// <returns>Domain identity.</returns>
    public static INdsIdentity Deserialize(string serialized)
    {
        return NdsIdentity.FromString(serialized);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-030] Create a proto-compatible identity tuple.
    /// </summary>
    public static (string Id, string Type, Dictionary<string, string> Metadata) ToProtoData(INdsIdentity identity)
    {
        return (
            identity.Id,
            identity.Type.ToProtoString(),
            new Dictionary<string, string>(identity.Metadata)
        );
    }

    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-032] Create a proto-compatible identity DTO.
    /// </summary>
    public static IdentityProtoDataV22 ToProtoDataV22(INdsIdentity identity)
    {
        return new IdentityProtoDataV22(
            Id: identity.Id,
            Type: identity.Type.ToProtoString(),
            Metadata: new Dictionary<string, string>(identity.Metadata),
            AttachedPolicyIds: identity.AttachedPolicyIds.ToArray()
        );
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-031] Create a domain identity from proto-compatible data.
    /// </summary>
    public static INdsIdentity FromProtoData(string id, string type, IReadOnlyDictionary<string, string>? metadata)
    {
        var identityType = IdentityTypeExtensions.FromString(type);
        return NdsIdentity.Of(id, identityType, metadata ?? new Dictionary<string, string>());
    }

    /// <summary>
    /// [Index: NDS-CSHARP-IDENTITYADAPTER-033] Create a domain identity from proto-compatible DTO.
    /// </summary>
    public static INdsIdentity FromProtoDataV22(IdentityProtoDataV22 data)
    {
        var identityType = IdentityTypeExtensions.FromString(data.Type);
        return NdsIdentity.Of(data.Id, identityType, data.Metadata)
            .WithAttachedPolicyIds(data.AttachedPolicyIds);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-IDENTITYADAPTER-120] Identity proto transport DTO.
/// </summary>
public sealed record IdentityProtoDataV22(
    string Id,
    string Type,
    Dictionary<string, string> Metadata,
    string[] AttachedPolicyIds
);
