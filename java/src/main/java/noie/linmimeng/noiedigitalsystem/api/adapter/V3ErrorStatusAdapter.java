package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.ErrorCategory;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.ErrorStatus;
import noie.linmimeng.noiedigitalsystem.api.result.NdsError;

/**
 * v3 ErrorStatus Adapter - NdsError ↔ v3 ErrorStatus.
 *
 * @since 3.0.0
 */
public final class V3ErrorStatusAdapter {

    private V3ErrorStatusAdapter() {
        // [Index] NDS-JAVA-V3-ERRORSTATUS-001 [Constraint] Utility class; instantiation is prohibited.
    }

    public static ErrorStatus toProto(NdsError error, ErrorCategory category, Integer retryAfterSeconds) {
        Objects.requireNonNull(error, "error");
        Objects.requireNonNull(category, "category");

        ErrorStatus.Builder builder = ErrorStatus.newBuilder()
            .setCode(error.code())
            .setMessage(error.message())
            .setCategory(category);

        if (retryAfterSeconds != null && retryAfterSeconds >= 0) {
            builder.setRetryAfterSeconds(retryAfterSeconds);
        }

        if (error.details() != null) {
            Map<String, String> details = new HashMap<>();
            for (Map.Entry<String, Object> entry : error.details().entrySet()) {
                details.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            builder.putAllDetails(details);
        }

        return builder.build();
    }

    public static NdsError fromProto(ErrorStatus status) {
        Objects.requireNonNull(status, "status");

        Map<String, Object> details = new HashMap<>();
        details.putAll(status.getDetailsMap());
        details.put("error_category", status.getCategory().name());
        if (status.hasRetryAfterSeconds()) {
            details.put("retry_after_seconds", status.getRetryAfterSeconds());
        }

        return NdsError.of(status.getCode(), status.getMessage(), details);
    }
}

