package noie.linmimeng.noiedigitalsystem.api.projection;

/**
 * [Index] NDS-JAVA-PROJECTIONID-000
 * [Semantic] Globally unique projection identifier (namespace:name).
 *
 * @since 2.0.0
 */
public interface ProjectionId {

    /** @return full ID string in "namespace:name" format (e.g. "balance:player:coins") */
    String value();

    /** @return namespace segment (e.g. "balance", "asset") */
    String namespace();

    /** @return name segment (e.g. "player:coins") */
    String name();

    /**
     * @param namespace namespace segment
     * @param name name segment
     * @return ProjectionId instance
     */
    static ProjectionId of(String namespace, String name) {
        return new ProjectionIdImpl(namespace, name);
    }

    /**
     * @param value non-empty "namespace:name" string
     * @return parsed ProjectionId
     * @throws IllegalArgumentException if value is null, empty, or malformed
     */
    static ProjectionId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ProjectionId cannot be null or empty");
        }
        int colonIndex = value.indexOf(':');
        if (colonIndex <= 0 || colonIndex >= value.length() - 1) {
            throw new IllegalArgumentException("Invalid ProjectionId format: " + value);
        }
        String namespace = value.substring(0, colonIndex);
        String name = value.substring(colonIndex + 1);
        return of(namespace, name);
    }
}

