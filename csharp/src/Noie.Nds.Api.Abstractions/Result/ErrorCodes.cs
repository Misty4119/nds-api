namespace Noie.Nds.Api.Result;

/// <summary>
/// [Index: NDS-CSHARP-ERRORCODES-000] Standard error codes.
/// 
/// <para>[Semantic] Shared, stable error code tokens used across SDKs and runtimes.</para>
/// </summary>
public static class ErrorCodes
{
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-010 [Semantic] Asset-related errors.
    // ========================================================================
    
    /// <summary>
    /// Asset not found.
    /// </summary>
    public const string AssetNotFound = "ASSET_NOT_FOUND";
    
    /// <summary>
    /// Insufficient balance.
    /// </summary>
    public const string InsufficientBalance = "INSUFFICIENT_BALANCE";
    
    /// <summary>
    /// Exceeds configured limit.
    /// </summary>
    public const string ExceedsLimit = "EXCEEDS_LIMIT";
    
    /// <summary>
    /// Asset already exists.
    /// </summary>
    public const string AssetAlreadyExists = "ASSET_ALREADY_EXISTS";
    
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-020 [Semantic] Identity-related errors.
    // ========================================================================
    
    /// <summary>
    /// Identity not found.
    /// </summary>
    public const string IdentityNotFound = "IDENTITY_NOT_FOUND";
    
    /// <summary>
    /// Invalid identity.
    /// </summary>
    public const string InvalidIdentity = "INVALID_IDENTITY";
    
    /// <summary>
    /// Permission denied.
    /// </summary>
    public const string PermissionDenied = "PERMISSION_DENIED";
    
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-030 [Semantic] Transaction-related errors.
    // ========================================================================
    
    /// <summary>
    /// Transaction failed.
    /// </summary>
    public const string TransactionFailed = "TRANSACTION_FAILED";
    
    /// <summary>
    /// Transaction timeout.
    /// </summary>
    public const string TransactionTimeout = "TRANSACTION_TIMEOUT";
    
    /// <summary>
    /// Transaction conflict.
    /// </summary>
    public const string TransactionConflict = "TRANSACTION_CONFLICT";
    
    /// <summary>
    /// Invalid amount.
    /// </summary>
    public const string InvalidAmount = "INVALID_AMOUNT";
    
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-040 [Semantic] Event-related errors.
    // ========================================================================
    
    /// <summary>
    /// Event not found.
    /// </summary>
    public const string EventNotFound = "EVENT_NOT_FOUND";
    
    /// <summary>
    /// Invalid event.
    /// </summary>
    public const string InvalidEvent = "INVALID_EVENT";
    
    /// <summary>
    /// Replay failed.
    /// </summary>
    public const string ReplayFailed = "REPLAY_FAILED";
    
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-050 [Semantic] Projection-related errors.
    // ========================================================================
    
    /// <summary>
    /// Projection not found.
    /// </summary>
    public const string ProjectionNotFound = "PROJECTION_NOT_FOUND";
    
    /// <summary>
    /// Projection already exists.
    /// </summary>
    public const string ProjectionAlreadyExists = "PROJECTION_ALREADY_EXISTS";
    
    // ========================================================================
    // [Index] NDS-CSHARP-ERRORCODES-060 [Semantic] System errors.
    // ========================================================================
    
    /// <summary>
    /// System error.
    /// </summary>
    public const string SystemError = "SYSTEM_ERROR";
    
    /// <summary>
    /// Internal error.
    /// </summary>
    public const string InternalError = "INTERNAL_ERROR";
    
    /// <summary>
    /// Service unavailable.
    /// </summary>
    public const string ServiceUnavailable = "SERVICE_UNAVAILABLE";
    
    /// <summary>
    /// Unknown error.
    /// </summary>
    public const string Unknown = "UNKNOWN";
    
    /// <summary>
    /// Invalid argument.
    /// </summary>
    public const string InvalidArgument = "INVALID_ARGUMENT";
    
    /// <summary>
    /// Operation cancelled.
    /// </summary>
    public const string OperationCancelled = "OPERATION_CANCELLED";
}
