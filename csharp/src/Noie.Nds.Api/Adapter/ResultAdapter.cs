using Noie.Nds.Api.Result;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-RESULTADAPTER-000] Result adapter (NdsResult/NdsError ↔ proto-compatible shapes).
/// 
/// <para>[Semantic] Conversion utilities for structured success/failure envelopes.</para>
/// </summary>
public static class ResultAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-RESULTADAPTER-010] Create a proto-compatible result tuple.
    /// </summary>
    public static (bool Success, string? ErrorCode, string? ErrorMessage, Dictionary<string, string>? Details) ToProtoData<T>(NdsResult<T> result)
    {
        if (result.IsSuccess)
        {
            return (true, null, null, null);
        }
        
        var details = new Dictionary<string, string>();
        foreach (var kvp in result.Error.Details)
        {
            details[kvp.Key] = kvp.Value?.ToString() ?? "";
        }
        
        return (false, result.Error.Code, result.Error.Message, details);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-RESULTADAPTER-020] Create a proto-compatible error tuple.
    /// </summary>
    public static (string Code, string Message, Dictionary<string, string> Details) ToProtoData(NdsError error)
    {
        var details = new Dictionary<string, string>();
        foreach (var kvp in error.Details)
        {
            details[kvp.Key] = kvp.Value?.ToString() ?? "";
        }
        
        return (error.Code, error.Message, details);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-RESULTADAPTER-021] Create a domain error from proto-compatible data.
    /// </summary>
    public static NdsError? FromProtoData(string? code, string? message, IReadOnlyDictionary<string, string>? details)
    {
        if (string.IsNullOrEmpty(code)) return null;
        
        var objectDetails = new Dictionary<string, object>();
        if (details != null)
        {
            foreach (var kvp in details)
            {
                objectDetails[kvp.Key] = kvp.Value;
            }
        }
        
        return NdsError.Of(code, message ?? "", objectDetails);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-RESULTADAPTER-022] Create a domain result from proto-compatible data.
    /// </summary>
    public static NdsResult<T> FromProtoData<T>(bool success, T? data, string? errorCode, string? errorMessage, IReadOnlyDictionary<string, string>? errorDetails)
    {
        if (success && data != null)
        {
            return NdsResult<T>.Success(data);
        }
        
        var error = FromProtoData(errorCode, errorMessage, errorDetails);
        return NdsResult<T>.Failure(error ?? NdsError.Of("UNKNOWN", "Unknown error"));
    }
}
