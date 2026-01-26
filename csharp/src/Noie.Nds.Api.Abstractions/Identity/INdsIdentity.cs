namespace Noie.Nds.Api.Identity;

/// <summary>
/// [Index: NDS-CSHARP-NDSIDENTITY-000] NDS Identity (future DID-ready).
/// 
/// <para>[Semantic] Represents any identity in the system (player/system/AI/external).</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] Must be immutable.</item>
///   <item>[Constraint] Id must be globally unique.</item>
///   <item>[Trace] Designed to support future DID extensions.</item>
/// </list>
/// </summary>
public interface INdsIdentity
{
    /// <summary>
    /// Get the identity ID.
    /// </summary>
    /// <returns>Globally unique identity ID (format depends on Type).</returns>
    string Id { get; }
    
    /// <summary>
    /// Get the identity type.
    /// </summary>
    IdentityType Type { get; }
    
    /// <summary>
    /// Get identity metadata.
    /// </summary>
    /// <returns>Metadata map (may be empty; never null).</returns>
    IReadOnlyDictionary<string, string> Metadata { get; }
    
    /// <summary>
    /// Check whether the identity is valid.
    /// </summary>
    bool IsValid => !string.IsNullOrEmpty(Id);
    
    /// <summary>
    /// Create a new identity instance with updated metadata (immutability-friendly).
    /// </summary>
    /// <param name="metadata">New metadata (nullable).</param>
    /// <returns>New identity instance.</returns>
    INdsIdentity WithMetadata(IReadOnlyDictionary<string, string>? metadata);
}

/// <summary>
/// [Index: NDS-CSHARP-NDSIDENTITY-100] NdsIdentity factory.
/// </summary>
public static class NdsIdentity
{
    /// <summary>
    /// [Index: NDS-CSHARP-NDSIDENTITY-110] Parse an identity from string ("type:id" or raw id).
    /// </summary>
    /// <param name="rawId">Raw ID string.</param>
    /// <returns>Identity instance.</returns>
    /// <exception cref="ArgumentException">If the format is invalid.</exception>
    public static INdsIdentity FromString(string rawId)
    {
        if (string.IsNullOrEmpty(rawId))
        {
            throw new ArgumentException("Identity ID cannot be null or empty", nameof(rawId));
        }
        
        if (rawId.Contains(':'))
        {
            var colonIndex = rawId.IndexOf(':');
            var typeStr = rawId[..colonIndex];
            var id = rawId[(colonIndex + 1)..];
            var type = IdentityTypeExtensions.FromString(typeStr);
            return Of(id, type);
        }
        
        // [Index] NDS-CSHARP-NDSIDENTITY-111 [Behavior] Default type is PLAYER when no prefix is present.
        return Of(rawId, IdentityType.Player);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSIDENTITY-120] Create an identity with an explicit type.
    /// </summary>
    /// <param name="id">Identity ID.</param>
    /// <param name="type">Identity type.</param>
    /// <returns>Identity instance.</returns>
    public static INdsIdentity Of(string id, IdentityType type)
    {
        return new NdsIdentityImpl(id, type, new Dictionary<string, string>());
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSIDENTITY-121] Create an identity with an explicit type and metadata.
    /// </summary>
    public static INdsIdentity Of(string id, IdentityType type, IReadOnlyDictionary<string, string> metadata)
    {
        return new NdsIdentityImpl(id, type, metadata);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSIDENTITY-900] NdsIdentity internal implementation.
/// </summary>
internal sealed record NdsIdentityImpl(
    string Id, 
    IdentityType Type, 
    IReadOnlyDictionary<string, string> Metadata
) : INdsIdentity
{
    public INdsIdentity WithMetadata(IReadOnlyDictionary<string, string>? metadata)
    {
        return new NdsIdentityImpl(Id, Type, metadata ?? new Dictionary<string, string>());
    }
}
