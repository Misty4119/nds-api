using Nds.Common.V1;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-V3-REQUESTCONTEXTADAPTER-000] v3 request context adapter.
///
/// <para>
/// [Semantic] Convenience helpers for creating <see cref="RequestContext"/> values used by v3 protocol messages.
/// </para>
/// </summary>
public static class V3RequestContextAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-V3-REQUESTCONTEXTADAPTER-010] Create a RequestContext.
    /// </summary>
    public static RequestContext Create(string requestId, string idempotencyKey, byte[]? correlationId = null)
    {
        if (string.IsNullOrWhiteSpace(requestId))
            throw new ArgumentException("requestId must be non-empty", nameof(requestId));
        if (string.IsNullOrWhiteSpace(idempotencyKey))
            throw new ArgumentException("idempotencyKey must be non-empty", nameof(idempotencyKey));

        var ctx = new RequestContext
        {
            RequestId = requestId,
            IdempotencyKey = idempotencyKey
        };
        if (correlationId is { Length: > 0 })
            ctx.CorrelationId = Google.Protobuf.ByteString.CopyFrom(correlationId);

        return ctx;
    }
}

