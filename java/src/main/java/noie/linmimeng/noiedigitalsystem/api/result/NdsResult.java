package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * [Index] NDS-JAVA-RESULT-000
 * [Semantic] Exceptionless business outcome envelope replacing checked exceptions.
 *
 * <p>[Constraint] API layer must not use checked exceptions for business failures.</p>
 * <p>[Behavior] Supports functional chaining via map/flatMap/onSuccess/onFailure.</p>
 *
 * @param <T> success data type
 * @since 2.0.0
 */
public interface NdsResult<T> {

    /** @return true if this result represents a successful outcome */
    boolean isSuccess();

    /**
     * @return success data
     * @throws IllegalStateException if this result is a failure
     */
    T data();

    /**
     * @return error details
     * @throws IllegalStateException if this result is a success
     */
    NdsError error();

    /**
     * @param mapper transformation applied to success data
     * @param <R> mapped type
     * @return new result with mapped data; propagates failure unchanged
     */
    <R> NdsResult<R> map(Function<T, R> mapper);

    /**
     * @param mapper transformation returning a new result
     * @param <R> mapped type
     * @return result from mapper; propagates failure unchanged
     */
    <R> NdsResult<R> flatMap(Function<T, NdsResult<R>> mapper);

    /**
     * @param consumer action to run on success
     * @return this result (for chaining)
     */
    NdsResult<T> onSuccess(Consumer<T> consumer);

    /**
     * @param consumer action to run on failure
     * @return this result (for chaining)
     */
    NdsResult<T> onFailure(Consumer<NdsError> consumer);

    /** @return Optional containing the success data; empty if failure */
    default Optional<T> toOptional() {
        return isSuccess() ? Optional.of(data()) : Optional.empty();
    }

    /**
     * @param data success payload
     * @param <T> data type
     * @return successful NdsResult
     */
    static <T> NdsResult<T> success(T data) {
        return new NdsResultImpl<>(true, data, null);
    }

    /**
     * @param error structured error
     * @param <T> data type
     * @return failed NdsResult
     */
    static <T> NdsResult<T> failure(NdsError error) {
        return new NdsResultImpl<>(false, null, error);
    }

    /**
     * @param code error code
     * @param message error message
     * @param <T> data type
     * @return failed NdsResult
     */
    static <T> NdsResult<T> failure(String code, String message) {
        return failure(NdsError.of(code, message));
    }
}

