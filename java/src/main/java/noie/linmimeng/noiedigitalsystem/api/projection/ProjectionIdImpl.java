package noie.linmimeng.noiedigitalsystem.api.projection;

/**
 * [Index] NDS-JAVA-PROJECTIONID-IMPL-000
 * [Semantic] Default implementation of {@link ProjectionId}.
 * 
 * @since 2.0.0
 */
final class ProjectionIdImpl implements ProjectionId {
    private final String namespace;
    private final String name;
    private final String value;
    
    ProjectionIdImpl(String namespace, String name) {
        if (namespace == null || namespace.isEmpty()) {
            throw new IllegalArgumentException("Projection namespace cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Projection name cannot be null or empty");
        }
        this.namespace = namespace;
        this.name = name;
        this.value = namespace + ":" + name;
    }
    
    @Override
    public String value() {
        return value;
    }
    
    @Override
    public String namespace() {
        return namespace;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectionIdImpl that = (ProjectionIdImpl) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return "ProjectionId{value='" + value + "'}";
    }
}

