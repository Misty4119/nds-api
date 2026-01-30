package noie.linmimeng.noiedigitalsystem.api.audit;

/**
 * [Index] NDS-JAVA-AUDIT-REF-IMPL-000
 * [Semantic] Default immutable {@link NdsRationaleRef} implementation.
 *
 * @since 2.2.0
 */
final class NdsRationaleRefImpl implements NdsRationaleRef {
    private final String uri;
    private final String hash;
    private final String mimeType;

    NdsRationaleRefImpl(String uri, String hash, String mimeType) {
        if (uri == null || uri.isEmpty()) {
            throw new IllegalArgumentException("uri cannot be null or empty");
        }
        this.uri = uri;
        this.hash = hash != null ? hash : "";
        this.mimeType = mimeType != null ? mimeType : "";
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public String hash() {
        return hash;
    }

    @Override
    public String mimeType() {
        return mimeType;
    }
}

