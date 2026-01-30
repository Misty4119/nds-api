using System;
using System.Collections.Generic;

namespace Noie.Nds.Api.Policy;

/// <summary>
/// [Index: NDS-CSHARP-NDSPOLICY-000] NDS Policy (data-only descriptor).
/// 
/// <para>[Semantic] Portable policy descriptor; evaluation semantics are implementation-defined.</para>
/// </summary>
public interface INdsPolicy
{
    string PolicyId { get; }
    string PolicyType { get; }
    IReadOnlyDictionary<string, string> Params { get; }
    byte[] CustomConfig { get; }
    IReadOnlyDictionary<string, string> Metadata { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSPOLICY-100] NdsPolicy factory.
/// </summary>
public static class NdsPolicy
{
    public static INdsPolicy Of(
        string policyId,
        string policyType,
        IReadOnlyDictionary<string, string>? @params = null,
        byte[]? customConfig = null,
        IReadOnlyDictionary<string, string>? metadata = null)
    {
        return new NdsPolicyImpl(
            policyId,
            policyType,
            @params ?? new Dictionary<string, string>(),
            customConfig ?? Array.Empty<byte>(),
            metadata ?? new Dictionary<string, string>());
    }
}

internal sealed record NdsPolicyImpl(
    string PolicyId,
    string PolicyType,
    IReadOnlyDictionary<string, string> Params,
    byte[] CustomConfig,
    IReadOnlyDictionary<string, string> Metadata
) : INdsPolicy;

