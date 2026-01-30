package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.Objects;
import noie.linmimeng.noiedigitalsystem.api.proto.event.v1.Cursor;

/**
 * v3 Event (v1 package) Adapter - minimal helpers for streaming primitives.
 *
 * @since 3.0.0
 */
public final class V3EventV1Adapter {

    private V3EventV1Adapter() {
        // [Index] NDS-JAVA-V3-EVENTV1-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * Create Cursor from opaque bytes.
     *
     * @param value opaque bytes (must be non-empty)
     * @return Cursor
     * @throws IllegalArgumentException if value is null/empty
     * @since 3.0.0
     */
    public static Cursor createCursor(byte[] value) {
        if (value == null || value.length == 0) {
            throw new IllegalArgumentException("value must be non-empty");
        }
        return Cursor.newBuilder()
            .setValue(com.google.protobuf.ByteString.copyFrom(value))
            .build();
    }

    /**
     * Extract opaque bytes from Cursor.
     *
     * @param cursor Cursor (non-null)
     * @return bytes
     * @since 3.0.0
     */
    public static byte[] toBytes(Cursor cursor) {
        Objects.requireNonNull(cursor, "cursor");
        return cursor.getValue().toByteArray();
    }
}

