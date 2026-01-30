using Nds.Ledger.V1;

namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-MONEYADAPTER-000] Money adapter (decimal ↔ v3 fixed-point Money proto DTO).
///
/// <para>
/// [Semantic] Converts between .NET <c>decimal</c> and the v3 fixed-point representation:
/// <c>amount = units + nanos / 1e9</c>.
/// </para>
///
/// <para>[Normative] See <c>spec/docs/V3.md</c> (Money invariants and exact conversion rules).</para>
/// </summary>
public static class MoneyAdapter
{
    private const int NanosScale = 9;
    private const int NanosLimit = 1_000_000_000;
    private const decimal NanosDivisor = 1_000_000_000m;

    /// <summary>
    /// [Index: NDS-CSHARP-MONEYADAPTER-010] Convert decimal to v3 Money message (exact; no rounding).
    /// </summary>
    /// <exception cref="ArgumentException">If <paramref name="currencyCode"/> is blank.</exception>
    /// <exception cref="ArgumentOutOfRangeException">If the value cannot be represented exactly with 9 fractional digits.</exception>
    public static Money ToProto(string currencyCode, decimal amount)
    {
        var dto = ToProtoData(currencyCode, amount);
        return new Money
        {
            CurrencyCode = dto.CurrencyCode,
            Units = dto.Units,
            Nanos = dto.Nanos
        };
    }

    /// <summary>
    /// [Index: NDS-CSHARP-MONEYADAPTER-011] Convert decimal to Money DTO (exact; no rounding).
    /// </summary>
    public static MoneyProtoData ToProtoData(string currencyCode, decimal amount)
    {
        if (string.IsNullOrWhiteSpace(currencyCode))
            throw new ArgumentException("currencyCode must be non-empty", nameof(currencyCode));

        var normalized = NormalizeForExactNanos(amount);

        if (normalized == 0m)
            return new MoneyProtoData(currencyCode, 0L, 0);

        var unitsDec = decimal.Truncate(normalized);
        if (unitsDec < long.MinValue || unitsDec > long.MaxValue)
            throw new ArgumentOutOfRangeException(nameof(amount), "units is out of int64 range");

        var units = (long)unitsDec;
        var frac = normalized - unitsDec; // exact for decimal
        var nanosDec = frac * NanosDivisor;

        // Truncate toward zero. For scale<=9 this is exact (no rounding required).
        var nanos = (int)decimal.Truncate(nanosDec);

        if (nanos <= -NanosLimit || nanos >= NanosLimit)
            throw new ArgumentOutOfRangeException(nameof(amount), $"nanos out of range: {nanos}");

        return new MoneyProtoData(currencyCode, units, nanos);
    }

    /// <summary>
    /// [Index: NDS-CSHARP-MONEYADAPTER-020] Convert v3 Money message to decimal (exact).
    /// </summary>
    public static decimal FromProto(Money proto)
    {
        if (proto == null) throw new ArgumentNullException(nameof(proto));
        return FromProtoData(new MoneyProtoData(proto.CurrencyCode, proto.Units, proto.Nanos));
    }

    /// <summary>
    /// [Index: NDS-CSHARP-MONEYADAPTER-021] Convert Money DTO to decimal (exact).
    /// </summary>
    public static decimal FromProtoData(MoneyProtoData proto)
    {
        if (string.IsNullOrWhiteSpace(proto.CurrencyCode))
            throw new ArgumentException("currencyCode must be non-empty", nameof(proto));

        if (proto.Nanos <= -NanosLimit || proto.Nanos >= NanosLimit)
            throw new ArgumentOutOfRangeException(nameof(proto), $"nanos out of range: {proto.Nanos}");

        // Reject ambiguous sign encodings such as units<0 with nanos>0 (and vice versa).
        if (proto.Units > 0 && proto.Nanos < 0)
            throw new ArgumentException("inconsistent sign: units>0 but nanos<0", nameof(proto));
        if (proto.Units < 0 && proto.Nanos > 0)
            throw new ArgumentException("inconsistent sign: units<0 but nanos>0", nameof(proto));

        if (proto.Units == 0L && proto.Nanos == 0)
            return 0m;

        return proto.Units + (proto.Nanos / NanosDivisor);
    }

    private static decimal NormalizeForExactNanos(decimal value)
    {
        // Exact conversion requires <= 9 fractional digits, after removing trailing zeros.
        // We implement this by truncating to 9 digits and ensuring it does not change the numeric value.
        var scale = GetScale(value);
        if (scale <= NanosScale) return value;

        var truncated = decimal.Round(value, NanosScale, MidpointRounding.ToZero);
        if (truncated != value)
            throw new ArgumentOutOfRangeException(nameof(value), "value has more than 9 fractional digits (exact conversion required)");

        return truncated;
    }

    private static int GetScale(decimal value)
    {
        var bits = decimal.GetBits(value);
        return (bits[3] >> 16) & 0x7F;
    }
}

/// <summary>
/// [Index: NDS-CSHARP-MONEYADAPTER-100] v3 Money transport DTO (proto shape).
/// </summary>
public sealed record MoneyProtoData(
    string CurrencyCode,
    long Units,
    int Nanos
);

