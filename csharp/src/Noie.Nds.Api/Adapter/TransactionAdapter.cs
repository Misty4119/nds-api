using Noie.Nds.Api.Asset;
using Noie.Nds.Api.Event;
using Noie.Nds.Api.Identity;
using Noie.Nds.Api.Transaction;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-TRANSACTIONADAPTER-000] Transaction adapter (INdsTransaction ↔ proto-compatible DTO).
/// 
/// <para>[Semantic] Bidirectional conversion between domain transactions and proto transport shapes.</para>
/// </summary>
public static class TransactionAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-010] Convert domain ConsistencyMode to a proto string token.
    /// </summary>
    public static string ToProtoString(ConsistencyMode mode)
    {
        return mode.ToProtoString();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-011] Convert a proto string token to domain ConsistencyMode.
    /// </summary>
    public static ConsistencyMode FromProtoString(string? protoMode)
    {
        return ConsistencyModeExtensions.FromString(protoMode);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-020] Convert domain TransactionStatus to a proto string token.
    /// </summary>
    public static string StatusToProtoString(TransactionStatus status)
    {
        return status.ToProtoString();
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-021] Convert a proto string token to domain TransactionStatus.
    /// </summary>
    public static TransactionStatus StatusFromProtoString(string? protoStatus)
    {
        return TransactionStatusExtensions.FromString(protoStatus);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-030] Create a proto-compatible transaction DTO.
    /// </summary>
    public static TransactionProtoData ToProtoData(INdsTransaction transaction)
    {
        var eventData = EventAdapter.ToProtoData(transaction);
        var (assetName, assetScope) = AssetAdapter.ToProtoData(transaction.Asset);
        
        return new TransactionProtoData(
            EventData: eventData,
            AssetName: assetName,
            AssetScope: assetScope,
            Delta: DecimalAdapter.ToProtoString(transaction.Delta),
            Consistency: transaction.Consistency.ToProtoString(),
            Status: transaction.Status.ToProtoString(),
            SourceId: transaction.Source?.Id,
            SourceType: transaction.Source?.Type.ToProtoString(),
            TargetId: transaction.Target?.Id,
            TargetType: transaction.Target?.Type.ToProtoString(),
            Reason: transaction.Reason
        );
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-TRANSACTIONADAPTER-031] Create a domain transaction from a proto-compatible DTO.
    /// </summary>
    public static INdsTransaction FromProtoData(TransactionProtoData data)
    {
        var eventId = EventAdapter.FromProtoData(data.EventData.EventIdValue, data.EventData.EventIdTimestamp);
        var occurredAt = DateTimeOffset.FromUnixTimeMilliseconds(data.EventData.OccurredAtMillis);
        var actor = IdentityAdapter.FromProtoData(
            data.EventData.ActorId, 
            data.EventData.ActorType, 
            data.EventData.ActorMetadata);
        
        var payloadDict = data.EventData.PayloadData.ToDictionary(
            kvp => kvp.Key, 
            kvp => (object?)kvp.Value);
        var payload = NdsPayload.Of(payloadDict);
        
        var asset = AssetAdapter.FromProtoData(data.AssetName, data.AssetScope);
        var delta = DecimalAdapter.FromProtoString(data.Delta);
        var consistency = ConsistencyModeExtensions.FromString(data.Consistency);
        var status = TransactionStatusExtensions.FromString(data.Status);
        
        return NdsTransaction.Builder()
            .Id(eventId)
            .OccurredAt(occurredAt)
            .Actor(actor)
            .Payload(payload)
            .SchemaVersion(data.EventData.SchemaVersion)
            .Asset(asset)
            .Delta(delta)
            .Consistency(consistency)
            .Status(status)
            .Build();
    }
}

/// <summary>
/// [Index: NDS-CSHARP-TRANSACTIONADAPTER-100] Transaction proto transport DTO.
/// </summary>
public sealed record TransactionProtoData(
    EventProtoData EventData,
    string AssetName,
    string AssetScope,
    string Delta,
    string Consistency,
    string Status,
    string? SourceId,
    string? SourceType,
    string? TargetId,
    string? TargetType,
    string? Reason
);
