package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.Objects;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.RequestContext;

/**
 * v3 RequestContext Adapter - helpers for building RequestContext.
 *
 * <p>Used by v3 protocol messages to carry tracing + idempotency correlation.</p>
 *
 * @since 3.0.0
 */
public final class V3RequestContextAdapter {

    private V3RequestContextAdapter() {
        // [Index] NDS-JAVA-V3-REQUESTCTX-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * Create a RequestContext (no correlation id).
     *
     * @since 3.0.0
     */
    public static RequestContext create(String requestId, String idempotencyKey) {
        Objects.requireNonNull(requestId, "requestId");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        if (requestId.isBlank()) {
            throw new IllegalArgumentException("requestId must be non-empty");
        }
        if (idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey must be non-empty");
        }
        return RequestContext.newBuilder()
            .setRequestId(requestId)
            .setIdempotencyKey(idempotencyKey)
            .build();
    }

    /**
     * Create a RequestContext with an optional correlation id.
     *
     * <p>[Behavior] When {@code correlationId} is null or empty, the field is left as the default empty bytes.</p>
     *
     * @since 3.0.0
     */
    public static RequestContext create(String requestId, String idempotencyKey, byte[] correlationId) {
        Objects.requireNonNull(requestId, "requestId");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        if (requestId.isBlank()) {
            throw new IllegalArgumentException("requestId must be non-empty");
        }
        if (idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey must be non-empty");
        }

        RequestContext.Builder builder = RequestContext.newBuilder()
            .setRequestId(requestId)
            .setIdempotencyKey(idempotencyKey);

        if (correlationId != null && correlationId.length > 0) {
            builder.setCorrelationId(com.google.protobuf.ByteString.copyFrom(correlationId));
        }

        return builder.build();
    }
}

