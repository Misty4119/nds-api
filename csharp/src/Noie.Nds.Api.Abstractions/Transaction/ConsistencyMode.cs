namespace Noie.Nds.Api.Transaction;

/// <summary>
/// [Index: NDS-CSHARP-CONSISTENCYMODE-000] Consistency mode.
/// 
/// <para>[Semantic] Consistency requirements for applying a transaction.</para>
/// </summary>
public enum ConsistencyMode
{
    /// <summary>
    /// Strong consistency.
    /// Applies immediately; prioritizes correctness.
    /// Suitable for critical economic operations (payments, transfers).
    /// </summary>
    Strong,
    
    /// <summary>
    /// Eventual consistency.
    /// May apply with delay; converges over time.
    /// Suitable for non-critical operations (stats, logs).
    /// </summary>
    Eventual,
    
    /// <summary>
    /// Optimistic consistency.
    /// Based on optimistic concurrency; may fail.
    /// Suitable for high-concurrency scenarios.
    /// </summary>
    Optimistic
}

/// <summary>
/// [Index: NDS-CSHARP-CONSISTENCYMODE-100] ConsistencyMode extensions.
/// </summary>
public static class ConsistencyModeExtensions
{
    /// <summary>
    /// [Index: NDS-CSHARP-CONSISTENCYMODE-110] Parse a consistency mode token.
    /// </summary>
    public static ConsistencyMode FromString(string? str)
    {
        if (string.IsNullOrEmpty(str)) return ConsistencyMode.Strong;
        
        return str.ToUpperInvariant() switch
        {
            "STRONG" => ConsistencyMode.Strong,
            "EVENTUAL" => ConsistencyMode.Eventual,
            "OPTIMISTIC" => ConsistencyMode.Optimistic,
            _ => ConsistencyMode.Strong
        };
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-CONSISTENCYMODE-120] Convert to proto string token.
    /// </summary>
    public static string ToProtoString(this ConsistencyMode mode)
    {
        return mode switch
        {
            ConsistencyMode.Strong => "STRONG",
            ConsistencyMode.Eventual => "EVENTUAL",
            ConsistencyMode.Optimistic => "OPTIMISTIC",
            _ => "STRONG"
        };
    }
}
