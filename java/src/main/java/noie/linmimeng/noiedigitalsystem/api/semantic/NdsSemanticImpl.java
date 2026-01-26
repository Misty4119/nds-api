package noie.linmimeng.noiedigitalsystem.api.semantic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * [Index] NDS-JAVA-SEMANTIC-IMPL-000
 * [Semantic] Default implementation of {@link NdsSemantic}.
 * 
 * @since 2.0.0
 */
final class NdsSemanticImpl implements NdsSemantic {
    private final Map<String, String> context;
    private final Optional<NdsTensor> embedding;
    
    NdsSemanticImpl(Map<String, String> context, Optional<NdsTensor> embedding) {
        this.context = context != null ? Collections.unmodifiableMap(new HashMap<>(context)) : Map.of();
        this.embedding = embedding != null ? embedding : Optional.empty();
    }
    
    @Override
    public Map<String, String> context() {
        return context;
    }
    
    @Override
    public Optional<NdsTensor> embedding() {
        return embedding;
    }
    
    @Override
    public String toString() {
        return "NdsSemantic{context=" + context + ", embedding=" + embedding.isPresent() + "}";
    }
}

