namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-ADAPTERS-000] Adapters entry point for Proto ↔ Domain conversions.
/// 
/// <para>[Semantic] Provides a consistent, discoverable surface for accessing adapter utilities.</para>
/// 
/// <h2>Examples</h2>
/// <code>
/// // [Index: NDS-CSHARP-ADAPTERS-EX-001] Decimal conversion.
/// string protoDecimal = DecimalAdapter.ToProtoString(100.5m);
/// decimal value = DecimalAdapter.FromProtoString(protoDecimal);
/// 
/// // [Index: NDS-CSHARP-ADAPTERS-EX-002] Identity conversion.
/// var protoData = IdentityAdapter.ToProtoData(domainIdentity);
/// var domainIdentity = IdentityAdapter.FromProtoData(id, type, metadata);
/// 
/// // [Index: NDS-CSHARP-ADAPTERS-EX-003] Event conversion.
/// var eventData = EventAdapter.ToProtoData(domainEvent);
/// var domainEvent = EventAdapter.FromProtoData(eventData);
/// 
/// // [Index: NDS-CSHARP-ADAPTERS-EX-004] Transaction conversion.
/// var txData = TransactionAdapter.ToProtoData(domainTx);
/// var domainTx = TransactionAdapter.FromProtoData(txData);
/// </code>
/// 
/// <para><b>Available adapters:</b></para>
/// <list type="bullet">
///   <item><see cref="DecimalAdapter"/> - decimal ↔ Proto Decimal</item>
///   <item><see cref="MoneyAdapter"/> - decimal ↔ v3 fixed-point Money</item>
///   <item><see cref="V3RequestContextAdapter"/> - helpers for v3 RequestContext</item>
///   <item><see cref="V3ErrorStatusAdapter"/> - NdsError ↔ v3 ErrorStatus</item>
///   <item><see cref="V3IdentityV1Adapter"/> - v3 identity primitives (PersonaId)</item>
///   <item><see cref="V3EventV1Adapter"/> - v3 event primitives (Cursor)</item>
///   <item><see cref="V3SyncV1Adapter"/> - v3 sync primitives (ResumeToken)</item>
///   <item><see cref="IdentityAdapter"/> - INdsIdentity ↔ Proto NdsIdentity</item>
///   <item><see cref="AssetAdapter"/> - IAssetId ↔ Proto AssetId</item>
///   <item><see cref="ContextAdapter"/> - INdsContext ↔ Proto NdsContext</item>
///   <item><see cref="EventAdapter"/> - INdsEvent ↔ Proto NdsEvent</item>
///   <item><see cref="TransactionAdapter"/> - INdsTransaction ↔ Proto NdsTransaction</item>
///   <item><see cref="ResultAdapter"/> - NdsResult ↔ Proto NdsResult</item>
/// </list>
/// </summary>
public static class Adapters
{
    // [Index] NDS-CSHARP-ADAPTERS-010 [Behavior] All adapters are static; call methods directly on the adapter type.
    // [Index] NDS-CSHARP-ADAPTERS-011 [Trace] Example: DecimalAdapter.ToProtoString(value)
}
