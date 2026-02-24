package noie.linmimeng.noiedigitalsystem.api.semantic;

import java.util.Map;
import java.util.Optional;

/**
 * [Index] NDS-JAVA-SEMANTIC-000
 * [Semantic] Semantic data carrier supporting AI context and optional vector embeddings.
 *
 * <p>[Constraint] Implementations must be immutable.</p>
 *
 * @since 2.0.0
 */
public interface NdsSemantic {

    /**
     * @return context key-value map (never null; may be empty)
     */
    Map<String, String> context();

    /**
     * @return vector embedding if present; {@link Optional#empty()} otherwise
     */
    Optional<NdsTensor> embedding();

    /**
     * @param key context key
     * @return context value; null if key is absent
     */
    default String getContext(String key) {
        return context().get(key);
    }

    /**
     * @param key context key
     * @return true if the key is present in the context map
     */
    default boolean hasContext(String key) {
        return context().containsKey(key);
    }

    /**
     * @param context context map
     * @return NdsSemantic without embedding
     */
    static NdsSemantic of(Map<String, String> context) {
        return new NdsSemanticImpl(context, Optional.empty());
    }

    /**
     * @param context context map
     * @param embedding vector embedding
     * @return NdsSemantic with embedding
     */
    static NdsSemantic of(Map<String, String> context, NdsTensor embedding) {
        return new NdsSemanticImpl(context, Optional.of(embedding));
    }
}

