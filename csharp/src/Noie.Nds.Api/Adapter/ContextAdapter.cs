using Noie.Nds.Api.Context;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-CONTEXTADAPTER-000] Context adapter (INdsContext ↔ proto-compatible shapes).
/// 
/// <para>[Semantic] Conversion utilities for tracing/correlation context.</para>
/// </summary>
public static class ContextAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-CONTEXTADAPTER-010] Create a proto-compatible context tuple.
    /// </summary>
    public static (string TraceId, string CorrelationId, Dictionary<string, string> Meta) ToProtoData(INdsContext context)
    {
        return (
            context.TraceId,
            context.CorrelationId,
            new Dictionary<string, string>(context.Meta)
        );
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-CONTEXTADAPTER-011] Create a domain context from proto-compatible data.
    /// </summary>
    public static INdsContext FromProtoData(string traceId, string correlationId, IReadOnlyDictionary<string, string>? meta)
    {
        var context = NdsContext.Create(traceId, correlationId);
        if (meta != null && meta.Count > 0)
        {
            context = context.WithMeta(meta);
        }
        return context;
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-CONTEXTADAPTER-012] Create a domain context from proto-compatible data (Dictionary overload).
    /// </summary>
    public static INdsContext FromProtoData(string traceId, string correlationId, Dictionary<string, string>? meta)
    {
        return FromProtoData(traceId, correlationId, (IReadOnlyDictionary<string, string>?)meta);
    }
}
