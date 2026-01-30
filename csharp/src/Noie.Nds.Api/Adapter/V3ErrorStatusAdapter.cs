using Nds.Common.V1;
using Noie.Nds.Api.Result;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-V3-ERRORSTATUSADAPTER-000] v3 error status adapter.
///
/// <para>[Semantic] Convert between domain <see cref="NdsError"/> and v3 <see cref="ErrorStatus"/>.</para>
/// </summary>
public static class V3ErrorStatusAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-V3-ERRORSTATUSADAPTER-010] Convert domain error to v3 ErrorStatus.
    /// </summary>
    public static ErrorStatus ToProto(NdsError error, ErrorCategory category, int? retryAfterSeconds = null)
    {
        ArgumentNullException.ThrowIfNull(error);

        var status = new ErrorStatus
        {
            Code = error.Code,
            Message = error.Message,
            Category = category
        };

        if (retryAfterSeconds is >= 0)
            status.RetryAfterSeconds = retryAfterSeconds.Value;

        foreach (var (key, value) in error.Details)
            status.Details[key] = value?.ToString() ?? string.Empty;

        return status;
    }

    /// <summary>
    /// [Index: NDS-CSHARP-V3-ERRORSTATUSADAPTER-011] Convert v3 ErrorStatus to domain error.
    /// </summary>
    public static NdsError FromProto(ErrorStatus status)
    {
        ArgumentNullException.ThrowIfNull(status);

        var details = new Dictionary<string, object>();
        foreach (var (key, value) in status.Details)
            details[key] = value;

        // Category/retryAfter are part of v3 semantics; preserve as structured details.
        details["error_category"] = status.Category.ToString();
        if (status.RetryAfterSeconds != 0)
            details["retry_after_seconds"] = status.RetryAfterSeconds;

        return NdsError.Of(status.Code, status.Message, details);
    }
}

