namespace Noie.Nds.Api.Event;

/// <summary>
/// [Index: NDS-CSHARP-NDSPAYLOAD-000] NDS payload.
/// 
/// <para>[Semantic] Container for event data (JSON-like key/value map).</para>
/// 
/// <para><b>Allowed types:</b></para>
/// <list type="bullet">
///   <item>String / integer / floating-point / boolean</item>
///   <item>Decimal (string representation)</item>
///   <item>Lists and maps</item>
///   <item>null</item>
/// </list>
/// 
/// <para><b>Forbidden types:</b></para>
/// <list type="bullet">
///   <item>Platform-specific objects (e.g., Minecraft Entity/Location)</item>
///   <item>Non-serializable objects</item>
/// </list>
/// </summary>
public sealed class NdsPayload
{
    private readonly IReadOnlyDictionary<string, object?> _data;
    
    /// <summary>
    /// Get payload data.
    /// </summary>
    public IReadOnlyDictionary<string, object?> Data => _data;
    
    private NdsPayload(IReadOnlyDictionary<string, object?> data)
    {
        _data = data;
    }
    
    /// <summary>
    /// Get a string value.
    /// </summary>
    public string? GetString(string key)
    {
        return _data.TryGetValue(key, out var value) ? value?.ToString() : null;
    }
    
    /// <summary>
    /// Get an int value.
    /// </summary>
    public int? GetInt(string key)
    {
        if (_data.TryGetValue(key, out var value) && value != null)
        {
            if (value is int i) return i;
            if (int.TryParse(value.ToString(), out var parsed)) return parsed;
        }
        return null;
    }
    
    /// <summary>
    /// Get a long value.
    /// </summary>
    public long? GetLong(string key)
    {
        if (_data.TryGetValue(key, out var value) && value != null)
        {
            if (value is long l) return l;
            if (long.TryParse(value.ToString(), out var parsed)) return parsed;
        }
        return null;
    }
    
    /// <summary>
    /// Get a decimal value.
    /// </summary>
    public decimal? GetDecimal(string key)
    {
        if (_data.TryGetValue(key, out var value) && value != null)
        {
            if (value is decimal d) return d;
            if (decimal.TryParse(value.ToString(), System.Globalization.NumberStyles.Any,
                System.Globalization.CultureInfo.InvariantCulture, out var parsed)) return parsed;
        }
        return null;
    }
    
    /// <summary>
    /// Get a boolean value.
    /// </summary>
    public bool? GetBool(string key)
    {
        if (_data.TryGetValue(key, out var value) && value != null)
        {
            if (value is bool b) return b;
            if (bool.TryParse(value.ToString(), out var parsed)) return parsed;
        }
        return null;
    }
    
    /// <summary>
    /// Check whether a key exists.
    /// </summary>
    public bool ContainsKey(string key) => _data.ContainsKey(key);
    
    /// <summary>
    /// Get the raw value.
    /// </summary>
    public object? Get(string key) => _data.TryGetValue(key, out var value) ? value : null;
    
    /// <summary>
    /// Create an empty payload.
    /// </summary>
    public static NdsPayload Empty() => new(new Dictionary<string, object?>());
    
    /// <summary>
    /// Create a payload from a dictionary.
    /// </summary>
    public static NdsPayload Of(IReadOnlyDictionary<string, object?> data)
    {
        return new NdsPayload(new Dictionary<string, object?>(data));
    }
    
    /// <summary>
    /// Create a payload builder.
    /// </summary>
    public static NdsPayloadBuilder Builder() => new();
}

/// <summary>
/// [Index: NDS-CSHARP-NDSPAYLOAD-100] NdsPayload builder.
/// </summary>
public sealed class NdsPayloadBuilder
{
    private readonly Dictionary<string, object?> _data = new();
    
    /// <summary>
    /// Add a string value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, string? value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Add an int value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, int value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Add a long value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, long value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Add a decimal value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, decimal value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Add a boolean value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, bool value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Add any value.
    /// </summary>
    public NdsPayloadBuilder Put(string key, object? value)
    {
        _data[key] = value;
        return this;
    }
    
    /// <summary>
    /// Build the payload.
    /// </summary>
    public NdsPayload Build() => NdsPayload.Of(_data);
}
