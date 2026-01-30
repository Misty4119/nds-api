namespace Noie.Nds.Api.Result;

/// <summary>
/// [Index: NDS-CSHARP-NDSERROR-000] NDS error object.
/// 
/// <para>[Semantic] Structured error details (code/message/details).</para>
/// </summary>
public sealed record NdsError
{
    /// <summary>
    /// Get the machine-readable error code.
    /// </summary>
    /// <returns>Error code (e.g. "INSUFFICIENT_BALANCE", "ASSET_NOT_FOUND").</returns>
    public required string Code { get; init; }
    
    /// <summary>
    /// Get the human-readable error message.
    /// </summary>
    /// <returns>Error message.</returns>
    public required string Message { get; init; }
    
    /// <summary>
    /// Get structured error details.
    /// </summary>
    /// <returns>Details map (may be empty; never null).</returns>
    public IReadOnlyDictionary<string, object> Details { get; init; } = new Dictionary<string, object>();
    
    /// <summary>
    /// Get the original exception (if present).
    /// </summary>
    /// <returns>Original exception; null if not present.</returns>
    public Exception? Cause { get; init; }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSERROR-010] Create an error.
    /// </summary>
    /// <param name="code">Error code.</param>
    /// <param name="message">Error message.</param>
    /// <returns>Error instance.</returns>
    public static NdsError Of(string code, string message)
    {
        return new NdsError { Code = code, Message = message };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSERROR-011] Create an error with details.
    /// </summary>
    /// <param name="code">Error code.</param>
    /// <param name="message">Error message.</param>
    /// <param name="details">Structured details.</param>
    /// <returns>Error instance.</returns>
    public static NdsError Of(string code, string message, IReadOnlyDictionary<string, object> details)
    {
        return new NdsError { Code = code, Message = message, Details = details };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-NDSERROR-012] Create an error with a cause exception.
    /// </summary>
    /// <param name="code">Error code.</param>
    /// <param name="message">Error message.</param>
    /// <param name="cause">Original exception.</param>
    /// <returns>Error instance.</returns>
    public static NdsError Of(string code, string message, Exception cause)
    {
        return new NdsError { Code = code, Message = message, Cause = cause };
    }
    
    public override string ToString() => $"[{Code}] {Message}";
}
