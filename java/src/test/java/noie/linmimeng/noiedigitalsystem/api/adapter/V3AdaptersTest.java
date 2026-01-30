package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.Map;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.ErrorCategory;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.ErrorStatus;
import noie.linmimeng.noiedigitalsystem.api.proto.common.v1.RequestContext;
import noie.linmimeng.noiedigitalsystem.api.result.NdsError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class V3AdaptersTest {

    @Test
    void requestContext_shouldCreate() {
        RequestContext ctx = V3RequestContextAdapter.create("req-1", "idem-1");
        assertEquals("req-1", ctx.getRequestId());
        assertEquals("idem-1", ctx.getIdempotencyKey());
    }

    @Test
    void requestContext_shouldCreateWithCorrelationId() {
        byte[] correlationId = new byte[] { 1, 2, 3 };
        RequestContext ctx = V3RequestContextAdapter.create("req-1", "idem-1", correlationId);
        assertEquals("req-1", ctx.getRequestId());
        assertEquals("idem-1", ctx.getIdempotencyKey());
        assertArrayEquals(correlationId, ctx.getCorrelationId().toByteArray());
    }

    @Test
    void primitives_shouldRoundTripOpaqueBytes() {
        byte[] persona = new byte[] { 9, 9, 9 };
        assertArrayEquals(persona, V3IdentityV1Adapter.toBytes(V3IdentityV1Adapter.createPersonaId(persona)));

        byte[] cursor = new byte[] { 1, 2, 3, 4 };
        assertArrayEquals(cursor, V3EventV1Adapter.toBytes(V3EventV1Adapter.createCursor(cursor)));

        byte[] token = new byte[] { 7, 8, 9 };
        assertArrayEquals(token, V3SyncV1Adapter.toBytes(V3SyncV1Adapter.createResumeToken(token)));
    }

    @Test
    void errorStatus_shouldRoundTrip() {
        NdsError domain = NdsError.of("SYSTEM_TIMEOUT", "timeout", Map.of("k", "v"));
        ErrorStatus proto = V3ErrorStatusAdapter.toProto(domain, ErrorCategory.ERROR_CATEGORY_RETRYABLE, 3);
        assertEquals("SYSTEM_TIMEOUT", proto.getCode());
        assertEquals(ErrorCategory.ERROR_CATEGORY_RETRYABLE, proto.getCategory());
        assertTrue(proto.hasRetryAfterSeconds());

        NdsError back = V3ErrorStatusAdapter.fromProto(proto);
        assertEquals("SYSTEM_TIMEOUT", back.code());
        assertEquals("timeout", back.message());
        assertTrue(back.details().containsKey("error_category"));
    }
}

