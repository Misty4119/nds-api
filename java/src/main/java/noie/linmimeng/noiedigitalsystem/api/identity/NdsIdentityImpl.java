package noie.linmimeng.noiedigitalsystem.api.identity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * [Index] NDS-JAVA-IDENTITY-IMPL-000
 * [Semantic] Default implementation of {@link NdsIdentity}.
 * 
 * @since 2.0.0
 */
final class NdsIdentityImpl implements NdsIdentity {
    private final String id;
    private final IdentityType type;
    private final Map<String, String> metadata;
    
    NdsIdentityImpl(String id, IdentityType type, Map<String, String> metadata) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Identity ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Identity type cannot be null");
        }
        this.id = id;
        this.type = type;
        this.metadata = metadata != null ? Collections.unmodifiableMap(new HashMap<>(metadata)) : Map.of();
    }
    
    @Override
    public String id() {
        return id;
    }
    
    @Override
    public IdentityType type() {
        return type;
    }
    
    @Override
    public Map<String, String> metadata() {
        return metadata;
    }
    
    @Override
    public NdsIdentity withMetadata(Map<String, String> metadata) {
        return new NdsIdentityImpl(id, type, metadata);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdsIdentityImpl that = (NdsIdentityImpl) o;
        return id.equals(that.id) && type == that.type;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode() * 31 + type.hashCode();
    }
    
    @Override
    public String toString() {
        return "NdsIdentity{id='" + id + "', type=" + type + "}";
    }
}

