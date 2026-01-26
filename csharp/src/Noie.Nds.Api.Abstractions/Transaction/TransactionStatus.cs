namespace Noie.Nds.Api.Transaction;

/// <summary>
/// [Index: NDS-CSHARP-TRANSACTIONSTATUS-000] Transaction status.
/// 
/// <para>[Semantic] Defines the lifecycle status of a transaction.</para>
/// </summary>
public enum TransactionStatus
{
    /// <summary>
    /// Pending.
    /// Created but not processed yet.
    /// </summary>
    Pending,
    
    /// <summary>
    /// Processing.
    /// Currently being processed.
    /// </summary>
    Processing,
    
    /// <summary>
    /// Completed.
    /// Successfully completed.
    /// </summary>
    Completed,
    
    /// <summary>
    /// Failed.
    /// Processing failed.
    /// </summary>
    Failed,
    
    /// <summary>
    /// Cancelled.
    /// Cancelled by caller/system.
    /// </summary>
    Cancelled,
    
    /// <summary>
    /// Rolled back.
    /// Rolled back after commit attempt.
    /// </summary>
    RolledBack
}

/// <summary>
/// [Index: NDS-CSHARP-TRANSACTIONSTATUS-100] TransactionStatus extensions.
/// </summary>
public static class TransactionStatusExtensions
{
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONSTATUS-110] Parse a transaction status token.
    /// </summary>
    public static TransactionStatus FromString(string? str)
    {
        if (string.IsNullOrEmpty(str)) return TransactionStatus.Pending;
        
        return str.ToUpperInvariant() switch
        {
            "PENDING" => TransactionStatus.Pending,
            "PROCESSING" => TransactionStatus.Processing,
            "COMPLETED" => TransactionStatus.Completed,
            "FAILED" => TransactionStatus.Failed,
            "CANCELLED" => TransactionStatus.Cancelled,
            "ROLLED_BACK" or "ROLLEDBACK" => TransactionStatus.RolledBack,
            _ => TransactionStatus.Pending
        };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONSTATUS-120] Convert to proto string token.
    /// </summary>
    public static string ToProtoString(this TransactionStatus status)
    {
        return status switch
        {
            TransactionStatus.Pending => "PENDING",
            TransactionStatus.Processing => "PROCESSING",
            TransactionStatus.Completed => "COMPLETED",
            TransactionStatus.Failed => "FAILED",
            TransactionStatus.Cancelled => "CANCELLED",
            TransactionStatus.RolledBack => "ROLLED_BACK",
            _ => "PENDING"
        };
    }
    
    /// <summary>
    /// Check whether the status is terminal.
    /// </summary>
    public static bool IsFinal(this TransactionStatus status)
    {
        return status is TransactionStatus.Completed 
            or TransactionStatus.Failed 
            or TransactionStatus.Cancelled 
            or TransactionStatus.RolledBack;
    }
}
