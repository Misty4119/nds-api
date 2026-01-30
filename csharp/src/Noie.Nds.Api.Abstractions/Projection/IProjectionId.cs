namespace Noie.Nds.Api.Projection;

/// <summary>
/// [Index: NDS-CSHARP-PROJECTIONID-000] Projection ID.
/// 
/// <para>[Semantic] Globally unique identifier for a projection.</para>
/// </summary>
public interface IProjectionId
{
    /// <summary>
    /// Get the projection ID value.
    /// </summary>
    string Value { get; }
}

/// <summary>
/// [Index: NDS-CSHARP-PROJECTIONID-100] ProjectionId factory.
/// </summary>
public static class ProjectionId
{
    /// <summary>
    /// [Index: NDS-CSHARP-PROJECTIONID-110] Create a projection ID from a value.
    /// </summary>
    public static IProjectionId Of(string value)
    {
        if (string.IsNullOrEmpty(value))
            throw new ArgumentException("ProjectionId value cannot be null or empty", nameof(value));
        return new ProjectionIdImpl(value);
    }
    
    /// <summary>
    /// [Index: NDS-CSHARP-PROJECTIONID-120] Generate a new projection ID.
    /// </summary>
    public static IProjectionId Generate()
    {
        return new ProjectionIdImpl(Guid.NewGuid().ToString());
    }
}

/// <summary>
/// [Index: NDS-CSHARP-PROJECTIONID-900] ProjectionId internal implementation.
/// </summary>
internal sealed record ProjectionIdImpl(string Value) : IProjectionId
{
    public override string ToString() => Value;
}
