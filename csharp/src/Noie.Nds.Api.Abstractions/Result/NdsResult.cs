namespace Noie.Nds.Api.Result;

/// <summary>
/// [Index: NDS-CSHARP-NDSRESULT-000] NDS Result (exceptionless business outcomes).
/// 
/// <para>[Semantic] Functional success/failure envelope replacing checked exceptions.</para>
/// 
/// <para><b>Constitutional constraints:</b></para>
/// <list type="bullet">
///   <item>[Constraint] API layer must not rely on checked exceptions for business failures.</item>
///   <item>[Behavior] Business outcomes are expressed via NdsResult.</item>
///   <item>[Behavior] Supports functional chaining.</item>
/// </list>
/// </summary>
/// <typeparam name="T">Data type on success.</typeparam>
public readonly struct NdsResult<T>
{
    private readonly T? _data;
    private readonly NdsError? _error;
    
    /// <summary>
    /// Check whether this result is successful.
    /// </summary>
    public bool IsSuccess { get; }
    
    /// <summary>
    /// Check whether this result is a failure.
    /// </summary>
    public bool IsFailure => !IsSuccess;
    
    /// <summary>
    /// Get the success data.
    /// </summary>
    /// <exception cref="InvalidOperationException">If the result is not successful.</exception>
    public T Data => IsSuccess 
        ? _data! 
        : throw new InvalidOperationException("Cannot access data on a failed result");
    
    /// <summary>
    /// Get the failure error.
    /// </summary>
    /// <exception cref="InvalidOperationException">If the result is successful.</exception>
    public NdsError Error => !IsSuccess 
        ? _error! 
        : throw new InvalidOperationException("Cannot access error on a successful result");
    
    private NdsResult(bool isSuccess, T? data, NdsError? error)
    {
        IsSuccess = isSuccess;
        _data = data;
        _error = error;
    }
    
    /// <summary>
    /// Map the success value.
    /// </summary>
    /// <typeparam name="TResult">New result type.</typeparam>
    /// <param name="mapper">Mapping function.</param>
    /// <returns>New result.</returns>
    public NdsResult<TResult> Map<TResult>(Func<T, TResult> mapper)
    {
        return IsSuccess 
            ? NdsResult<TResult>.Success(mapper(_data!)) 
            : NdsResult<TResult>.Failure(_error!);
    }
    
    /// <summary>
    /// Flat-map the success value.
    /// </summary>
    /// <typeparam name="TResult">New result type.</typeparam>
    /// <param name="mapper">Mapping function (returns a new result).</param>
    /// <returns>New result.</returns>
    public NdsResult<TResult> FlatMap<TResult>(Func<T, NdsResult<TResult>> mapper)
    {
        return IsSuccess 
            ? mapper(_data!) 
            : NdsResult<TResult>.Failure(_error!);
    }
    
    /// <summary>
    /// Run an action when successful.
    /// </summary>
    /// <param name="action">Action.</param>
    /// <returns>The current result.</returns>
    public NdsResult<T> OnSuccess(Action<T> action)
    {
        if (IsSuccess) action(_data!);
        return this;
    }
    
    /// <summary>
    /// Run an action when failed.
    /// </summary>
    /// <param name="action">Action.</param>
    /// <returns>The current result.</returns>
    public NdsResult<T> OnFailure(Action<NdsError> action)
    {
        if (!IsSuccess) action(_error!);
        return this;
    }
    
    /// <summary>
    /// Get the data or a default value.
    /// </summary>
    public T? GetOrDefault(T? defaultValue = default)
    {
        return IsSuccess ? _data : defaultValue;
    }
    
    /// <summary>
    /// Match success or failure.
    /// </summary>
    public TResult Match<TResult>(Func<T, TResult> onSuccess, Func<NdsError, TResult> onFailure)
    {
        return IsSuccess ? onSuccess(_data!) : onFailure(_error!);
    }
    
    /// <summary>
    /// Create a successful result.
    /// </summary>
    public static NdsResult<T> Success(T data)
    {
        return new NdsResult<T>(true, data, null);
    }
    
    /// <summary>
    /// Create a failed result.
    /// </summary>
    public static NdsResult<T> Failure(NdsError error)
    {
        return new NdsResult<T>(false, default, error);
    }
    
    /// <summary>
    /// Create a failed result (convenience overload).
    /// </summary>
    public static NdsResult<T> Failure(string code, string message)
    {
        return Failure(NdsError.Of(code, message));
    }
    
    /// <summary>
    /// Implicit conversion from T to NdsResult&lt;T&gt; (success).
    /// </summary>
    public static implicit operator NdsResult<T>(T value) => Success(value);
    
    /// <summary>
    /// Implicit conversion from NdsError to NdsResult&lt;T&gt; (failure).
    /// </summary>
    public static implicit operator NdsResult<T>(NdsError error) => Failure(error);
}

/// <summary>
/// [Index: NDS-CSHARP-NDSRESULT-100] NdsResult helpers.
/// </summary>
public static class NdsResult
{
    /// <summary>
    /// Create a successful result.
    /// </summary>
    public static NdsResult<T> Success<T>(T data) => NdsResult<T>.Success(data);
    
    /// <summary>
    /// Create a failed result.
    /// </summary>
    public static NdsResult<T> Failure<T>(NdsError error) => NdsResult<T>.Failure(error);
    
    /// <summary>
    /// Create a failed result.
    /// </summary>
    public static NdsResult<T> Failure<T>(string code, string message) 
        => NdsResult<T>.Failure(code, message);
    
    /// <summary>
    /// Create an empty successful result.
    /// </summary>
    public static NdsResult<Unit> Ok() => NdsResult<Unit>.Success(Unit.Value);
}

/// <summary>
/// [Index: NDS-CSHARP-UNIT-000] Represents an empty value (void-like).
/// </summary>
public readonly struct Unit
{
    /// <summary>
    /// The single Unit value.
    /// </summary>
    public static readonly Unit Value = new();
    
    public override string ToString() => "()";
}
