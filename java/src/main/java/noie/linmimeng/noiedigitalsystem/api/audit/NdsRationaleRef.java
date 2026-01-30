package noie.linmimeng.noiedigitalsystem.api.audit;

/**
 * [Index] NDS-JAVA-AUDIT-REF-000
 * [Semantic] External rationale/evidence reference (URI + integrity).
 *
 * @since 2.2.0
 */
public interface NdsRationaleRef {

    String uri();

    /**
     * Optional hash (e.g. sha256 hex).
     */
    String hash();

    /**
     * Optional mime type (e.g. application/json).
     */
    String mimeType();

    static NdsRationaleRef of(String uri, String hash, String mimeType) {
        return new NdsRationaleRefImpl(uri, hash, mimeType);
    }
}

