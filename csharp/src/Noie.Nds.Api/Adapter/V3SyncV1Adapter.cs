using Nds.Sync.V1;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-V3-SYNCV1ADAPTER-000] v3 sync (v1 package) adapter.
///
/// <para>
/// [Semantic] Minimal helpers for v3+ streaming primitives (ResumeToken).
/// </para>
/// </summary>
public static class V3SyncV1Adapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-V3-SYNCV1ADAPTER-010] Create a <see cref="ResumeToken"/> from opaque bytes.
    /// </summary>
    /// <exception cref="ArgumentException">If <paramref name="value"/> is null or empty.</exception>
    public static ResumeToken CreateResumeToken(byte[] value)
    {
        if (value is not { Length: > 0 })
            throw new ArgumentException("value must be non-empty", nameof(value));

        return new ResumeToken { Value = Google.Protobuf.ByteString.CopyFrom(value) };
    }

    /// <summary>
    /// [Index: NDS-CSHARP-V3-SYNCV1ADAPTER-011] Extract opaque bytes from a <see cref="ResumeToken"/>.
    /// </summary>
    public static byte[] ToBytes(ResumeToken token)
    {
        ArgumentNullException.ThrowIfNull(token);
        return token.Value.ToByteArray();
    }
}

