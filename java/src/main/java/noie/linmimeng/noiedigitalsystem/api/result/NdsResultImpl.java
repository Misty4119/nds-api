package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * [Index] NDS-JAVA-RESULT-IMPL-000
 * [Semantic] Default implementation of {@link NdsResult}.
 * 
 * @since 2.0.0
 */
final class NdsResultImpl<T> implements NdsResult<T> {
    private final boolean success;
    private final T data;
    private final NdsError error;
    
    NdsResultImpl(boolean success, T data, NdsError error) {
        if (success && error != null) {
            throw new IllegalArgumentException("Success result cannot have error");
        }
        if (!success && error == null) {
            throw new IllegalArgumentException("Failure result must have error");
        }
        this.success = success;
        this.data = data;
        this.error = error;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public T data() {
        if (!success) {
            throw new IllegalStateException("Cannot get data from failure result");
        }
        return data;
    }
    
    @Override
    public NdsError error() {
        if (success) {
            throw new IllegalStateException("Cannot get error from success result");
        }
        return error;
    }
    
    @Override
    public <R> NdsResult<R> map(Function<T, R> mapper) {
        if (success) {
            return NdsResult.success(mapper.apply(data));
        } else {
            return NdsResult.failure(error);
        }
    }
    
    @Override
    public <R> NdsResult<R> flatMap(Function<T, NdsResult<R>> mapper) {
        if (success) {
            return mapper.apply(data);
        } else {
            return NdsResult.failure(error);
        }
    }
    
    @Override
    public NdsResult<T> onSuccess(Consumer<T> consumer) {
        if (success) {
            consumer.accept(data);
        }
        return this;
    }
    
    @Override
    public NdsResult<T> onFailure(Consumer<NdsError> consumer) {
        if (!success) {
            consumer.accept(error);
        }
        return this;
    }
    
    @Override
    public Optional<T> toOptional() {
        return isSuccess() ? Optional.of(data) : Optional.empty();
    }
    
    @Override
    public String toString() {
        if (success) {
            return "NdsResult{success=true, data=" + data + "}";
        } else {
            return "NdsResult{success=false, error=" + error + "}";
        }
    }
}

