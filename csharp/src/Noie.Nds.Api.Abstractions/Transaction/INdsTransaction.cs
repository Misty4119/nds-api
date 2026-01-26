using Noie.Nds.Api.Asset;
using Noie.Nds.Api.Event;
using Noie.Nds.Api.Identity;

namespace Noie.Nds.Api.Transaction;

/// <summary>
/// [Index: NDS-CSHARP-NDSTRANSACTION-000] NDS Transaction (economic event).
/// 
/// <para>[Semantic] Economic event representing an asset delta/transfer.</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] Transaction is a specialized event.</item>
///   <item>[Behavior] Supports consistency modes.</item>
///   <item>[Constraint] Delta is exact decimal; positive=credit, negative=debit.</item>
/// </list>
/// </summary>
public interface INdsTransaction : INdsEvent
{
    /// <summary>
    /// Get the asset ID.
    /// </summary>
    IAssetId Asset { get; }
    
    /// <summary>
    /// Get the delta.
    /// 
    /// <para>Positive=credit, negative=debit.</para>
    /// </summary>
    /// <returns>Delta as decimal (precision-safe).</returns>
    decimal Delta { get; }
    
    /// <summary>
    /// Get the consistency mode.
    /// </summary>
    ConsistencyMode Consistency { get; }
    
    /// <summary>
    /// Get the transaction status.
    /// </summary>
    TransactionStatus Status { get; }
    
    /// <summary>
    /// Get the source identity (optional).
    /// 
    /// <para>For transfers, this is the sender.</para>
    /// </summary>
    INdsIdentity? Source => Payload.GetString("source") is { } s 
        ? NdsIdentity.FromString(s) 
        : null;
    
    /// <summary>
    /// Get the target identity (optional).
    /// 
    /// <para>For transfers, this is the receiver.</para>
    /// </summary>
    INdsIdentity? Target => Payload.GetString("target") is { } t 
        ? NdsIdentity.FromString(t) 
        : null;
    
    /// <summary>
    /// Get the reason (optional).
    /// </summary>
    string? Reason => Payload.GetString("reason");
}

/// <summary>
/// [Index: NDS-CSHARP-NDSTRANSACTION-100] NdsTransaction factory.
/// </summary>
public static class NdsTransaction
{
    /// <summary>
    /// Create a transaction builder.
    /// </summary>
    public static NdsTransactionBuilder Builder() => new();
}

/// <summary>
/// [Index: NDS-CSHARP-NDSTRANSACTION-200] NdsTransaction builder.
/// </summary>
public sealed class NdsTransactionBuilder
{
    private IEventId? _id;
    private DateTimeOffset _occurredAt = DateTimeOffset.UtcNow;
    private INdsIdentity? _actor;
    private NdsPayload _payload = NdsPayload.Empty();
    private int _schemaVersion = 1;
    private readonly Dictionary<string, string> _metadata = new();
    
    private IAssetId? _asset;
    private decimal _delta;
    private ConsistencyMode _consistency = ConsistencyMode.Strong;
    private TransactionStatus _status = TransactionStatus.Pending;
    
    public NdsTransactionBuilder Id(IEventId id)
    {
        _id = id;
        return this;
    }
    
    public NdsTransactionBuilder OccurredAt(DateTimeOffset occurredAt)
    {
        _occurredAt = occurredAt;
        return this;
    }
    
    public NdsTransactionBuilder Actor(INdsIdentity actor)
    {
        _actor = actor;
        return this;
    }
    
    public NdsTransactionBuilder Asset(IAssetId asset)
    {
        _asset = asset;
        return this;
    }
    
    public NdsTransactionBuilder Delta(decimal delta)
    {
        _delta = delta;
        return this;
    }
    
    public NdsTransactionBuilder Consistency(ConsistencyMode consistency)
    {
        _consistency = consistency;
        return this;
    }
    
    public NdsTransactionBuilder Status(TransactionStatus status)
    {
        _status = status;
        return this;
    }
    
    public NdsTransactionBuilder Payload(NdsPayload payload)
    {
        _payload = payload;
        return this;
    }
    
    public NdsTransactionBuilder SchemaVersion(int version)
    {
        _schemaVersion = version;
        return this;
    }
    
    public NdsTransactionBuilder WithMetadata(string key, string value)
    {
        _metadata[key] = value;
        return this;
    }
    
    public INdsTransaction Build()
    {
        if (_id == null) _id = EventId.Generate();
        if (_actor == null) throw new InvalidOperationException("Actor is required");
        if (_asset == null) throw new InvalidOperationException("Asset is required");
        
        return new NdsTransactionImpl(
            _id, _occurredAt, _actor, EventType.Transaction, _payload, _schemaVersion, _metadata,
            _asset, _delta, _consistency, _status);
    }
}

/// <summary>
/// [Index: NDS-CSHARP-NDSTRANSACTION-900] NdsTransaction internal implementation.
/// </summary>
internal sealed record NdsTransactionImpl(
    IEventId Id,
    DateTimeOffset OccurredAt,
    INdsIdentity Actor,
    EventType Type,
    NdsPayload Payload,
    int SchemaVersion,
    IReadOnlyDictionary<string, string> Metadata,
    IAssetId Asset,
    decimal Delta,
    ConsistencyMode Consistency,
    TransactionStatus Status
) : INdsTransaction;
