namespace Noie.Nds.Api.Adapter;

/// <summary>
/// [Index: NDS-CSHARP-DECIMALADAPTER-000] Decimal adapter (decimal ↔ proto string).
/// 
/// <para>[Constraint] Proto encodes decimals as strings to preserve exactness across languages.</para>
/// </summary>
public static class DecimalAdapter
{
    /// <summary>
    /// [Index: NDS-CSHARP-DECIMALADAPTER-010] Convert .NET decimal to proto string representation.
    /// </summary>
    /// <param name="value">Decimal value.</param>
    /// <returns>Invariant-culture string representation.</returns>
    public static string ToProtoString(decimal value)
    {
        return value.ToString(System.Globalization.CultureInfo.InvariantCulture);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-DECIMALADAPTER-011] Convert proto string representation to .NET decimal.
    /// </summary>
    /// <param name="value">String value.</param>
    /// <returns>Decimal value; returns 0 on parse failure.</returns>
    public static decimal FromProtoString(string? value)
    {
        if (string.IsNullOrEmpty(value)) return 0m;
        
        if (decimal.TryParse(value, System.Globalization.NumberStyles.Any, 
            System.Globalization.CultureInfo.InvariantCulture, out var result))
        {
            return result;
        }
        
        return 0m;
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-DECIMALADAPTER-012] Try to parse proto string representation into .NET decimal.
    /// </summary>
    /// <param name="value">String value.</param>
    /// <param name="result">Output decimal value.</param>
    /// <returns>True if parsing succeeds.</returns>
    public static bool TryFromProtoString(string? value, out decimal result)
    {
        result = 0m;
        if (string.IsNullOrEmpty(value)) return false;
        
        return decimal.TryParse(value, System.Globalization.NumberStyles.Any,
            System.Globalization.CultureInfo.InvariantCulture, out result);
    }
}
