using System;
using System.Collections.Generic;
using Noie.Nds.Api.Event;

namespace Noie.Nds.Api.Audit;

/// <summary>
/// [Index: NDS-CSHARP-NDSRATIONALE-000] Structured rationale (lightweight).
/// </summary>
public interface INdsRationale
{
    string Source { get; }
    decimal? Confidence { get; }
    IReadOnlyList<string> ThoughtPath { get; }
    IReadOnlyList<IEventId> EvidenceEventIds { get; }
    IReadOnlyList<INdsRationaleRef> EvidenceRefs { get; }
    decimal? RiskScore { get; }
    IReadOnlyDictionary<string, string> Metadata { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSRATIONALE-020] External rationale/evidence reference.
/// </summary>
public interface INdsRationaleRef
{
    string Uri { get; }
    string? Hash { get; }
    string? MimeType { get; }
}

public static class NdsRationale
{
    public static INdsRationale Of(
        string source,
        decimal? confidence = null,
        IReadOnlyList<string>? thoughtPath = null,
        IReadOnlyList<IEventId>? evidenceEventIds = null,
        IReadOnlyList<INdsRationaleRef>? evidenceRefs = null,
        decimal? riskScore = null,
        IReadOnlyDictionary<string, string>? metadata = null)
    {
        return new NdsRationaleImpl(
            source,
            confidence,
            thoughtPath ?? Array.Empty<string>(),
            evidenceEventIds ?? Array.Empty<IEventId>(),
            evidenceRefs ?? Array.Empty<INdsRationaleRef>(),
            riskScore,
            metadata ?? new Dictionary<string, string>());
    }
    
    public static INdsRationaleRef Ref(string uri, string? hash = null, string? mimeType = null)
    {
        return new NdsRationaleRefImpl(uri, hash, mimeType);
    }
}

internal sealed record NdsRationaleImpl(
    string Source,
    decimal? Confidence,
    IReadOnlyList<string> ThoughtPath,
    IReadOnlyList<IEventId> EvidenceEventIds,
    IReadOnlyList<INdsRationaleRef> EvidenceRefs,
    decimal? RiskScore,
    IReadOnlyDictionary<string, string> Metadata
) : INdsRationale;

internal sealed record NdsRationaleRefImpl(
    string Uri,
    string? Hash,
    string? MimeType
) : INdsRationaleRef;

