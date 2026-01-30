using Nds.Event.V1;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-V3-EVENTV1ADAPTER-000] v3 event (v1 package) adapter.
///
/// <para>
/// [Semantic] Minimal helpers for v3+ streaming primitives (Cursor).
/// </para>
/// </summary>
public static class V3EventV1Adapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-V3-EVENTV1ADAPTER-010] Create a <see cref="Cursor"/> from opaque bytes.
    /// </summary>
    /// <exception cref="ArgumentException">If <paramref name="value"/> is null or empty.</exception>
    public static Cursor CreateCursor(byte[] value)
    {
        if (value is not { Length: > 0 })
            throw new ArgumentException("value must be non-empty", nameof(value));

        return new Cursor { Value = Google.Protobuf.ByteString.CopyFrom(value) };
    }

    /// <summary>
    /// [Index: NDS-CSHARP-V3-EVENTV1ADAPTER-011] Extract opaque bytes from a <see cref="Cursor"/>.
    /// </summary>
    public static byte[] ToBytes(Cursor cursor)
    {
        ArgumentNullException.ThrowIfNull(cursor);
        return cursor.Value.ToByteArray();
    }
}

