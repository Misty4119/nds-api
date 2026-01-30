namespace Noie.Nds.Api.Context;

/// <summary>
/// [Index: NDS-CSHARP-NDSCONTEXT-000] NDS Context (tracing + correlation).
/// 
/// <para>[Semantic] Carries correlation metadata across call boundaries.</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] Context must be immutable.</item>
///   <item>[Behavior] Supports distributed tracing.</item>
///   <item>[Behavior] Supports context propagation.</item>
/// </list>
/// </summary>
public interface INdsContext
{
    /// <summary>
    /// Get the trace ID.
    /// 
    /// <para>Globally unique identifier for distributed tracing.</para>
    /// </summary>
    /// <returns>Trace ID (recommended: UUID).</returns>
    string TraceId { get; }
    
    /// <summary>
    /// Get the correlation ID.
    /// 
    /// <para>Correlates related operations; may be equal to TraceId or different.</para>
    /// </summary>
    string CorrelationId { get; }
    
    /// <summary>
    /// Get metadata entries.
    /// </summary>
    /// <returns>Metadata map (may be empty; never null).</returns>
    IReadOnlyDictionary<string, string> Meta { get; }
    
    /// <summary>
    /// Get a metadata value by key.
    /// </summary>
    /// <param name="key">Key.</param>
    /// <returns>Value, or null if not found.</returns>
    string? GetMeta(string key) => Meta.TryGetValue(key, out var value) ? value : null;
    
    /// <summary>
    /// Create a new context instance with additional metadata (immutability-friendly).
    /// </summary>
    /// <param name="additionalMeta">Additional metadata.</param>
    /// <returns>New context instance.</returns>
    INdsContext WithMeta(IReadOnlyDictionary<string, string> additionalMeta);
    
    /// <summary>
    /// Create a new context instance with a single metadata entry.
    /// </summary>
    /// <param name="key">Key.</param>
    /// <param name="value">Value.</param>
    /// <returns>New context instance.</returns>
    INdsContext WithMeta(string key, string value);
}

/// <summary>
/// [Index: NDS-CSHARP-NDSCONTEXT-100] NdsContext factory.
/// </summary>
public static class NdsContext
{
    /// <summary>
    /// [Index: NDS-CSHARP-NDSCONTEXT-110] Create a new context.
    /// </summary>
    /// <param name="traceId">Trace ID (auto-generated if null).</param>
    /// <param name="correlationId">Correlation ID (defaults to traceId if null).</param>
    /// <returns>New context instance.</returns>
    public static INdsContext Create(string? traceId = null, string? correlationId = null)
    {
        var tid = traceId ?? Guid.NewGuid().ToString();
        var cid = correlationId ?? tid;
        return new NdsContextImpl(tid, cid, new Dictionary<string, string>());
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSCONTEXT-120] Create a context from an existing trace ID.
    /// </summary>
    public static INdsContext FromTraceId(string traceId)
    {
        return new NdsContextImpl(traceId, traceId, new Dictionary<string, string>());
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSCONTEXT-900] NdsContext internal implementation.
/// </summary>
internal sealed record NdsContextImpl(
    string TraceId,
    string CorrelationId,
    IReadOnlyDictionary<string, string> Meta
) : INdsContext
{
    public INdsContext WithMeta(IReadOnlyDictionary<string, string> additionalMeta)
    {
        var newMeta = new Dictionary<string, string>(Meta);
        foreach (var kvp in additionalMeta)
        {
            newMeta[kvp.Key] = kvp.Value;
        }
        return new NdsContextImpl(TraceId, CorrelationId, newMeta);
    }
    
    public INdsContext WithMeta(string key, string value)
    {
        var newMeta = new Dictionary<string, string>(Meta) { [key] = value };
        return new NdsContextImpl(TraceId, CorrelationId, newMeta);
    }
}
