package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.Objects;
import noie.linmimeng.noiedigitalsystem.api.proto.sync.v1.ResumeToken;

/**
 * v3 Sync (v1 package) Adapter - minimal helpers for streaming primitives.
 *
 * @since 3.0.0
 */
public final class V3SyncV1Adapter {

    private V3SyncV1Adapter() {
        // [Index] NDS-JAVA-V3-SYNCV1-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * Create ResumeToken from opaque bytes.
     *
     * @param value opaque bytes (must be non-empty)
     * @return ResumeToken
     * @throws IllegalArgumentException if value is null/empty
     * @since 3.0.0
     */
    public static ResumeToken createResumeToken(byte[] value) {
        if (value == null || value.length == 0) {
            throw new IllegalArgumentException("value must be non-empty");
        }
        return ResumeToken.newBuilder()
            .setValue(com.google.protobuf.ByteString.copyFrom(value))
            .build();
    }

    /**
     * Extract opaque bytes from ResumeToken.
     *
     * @param token ResumeToken (non-null)
     * @return bytes
     * @since 3.0.0
     */
    public static byte[] toBytes(ResumeToken token) {
        Objects.requireNonNull(token, "token");
        return token.getValue().toByteArray();
    }
}

