using Nds.Identity.V1;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-V3-IDENTITYV1ADAPTER-000] v3 identity (v1 package) adapter.
///
/// <para>
/// [Semantic] Provides minimal, deterministic helpers for working with versioned v3+ identity primitives.
/// This adapter is intentionally transport-level (proto DTO ↔ raw bytes) and does not attempt runtime resolution.
/// </para>
/// </summary>
public static class V3IdentityV1Adapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-V3-IDENTITYV1ADAPTER-010] Create a <see cref="PersonaId"/> from opaque bytes.
    /// </summary>
    /// <exception cref="ArgumentException">If <paramref name="value"/> is null or empty.</exception>
    public static PersonaId CreatePersonaId(byte[] value)
    {
        if (value is not { Length: > 0 })
            throw new ArgumentException("value must be non-empty", nameof(value));

        return new PersonaId { Value = Google.Protobuf.ByteString.CopyFrom(value) };
    }

    /// <summary>
    /// [Index: NDS-CSHARP-V3-IDENTITYV1ADAPTER-011] Extract opaque bytes from a <see cref="PersonaId"/>.
    /// </summary>
    public static byte[] ToBytes(PersonaId personaId)
    {
        ArgumentNullException.ThrowIfNull(personaId);
        return personaId.Value.ToByteArray();
    }
}

