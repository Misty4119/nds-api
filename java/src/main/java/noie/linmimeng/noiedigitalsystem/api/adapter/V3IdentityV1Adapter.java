package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.util.Objects;
import noie.linmimeng.noiedigitalsystem.api.proto.identity.v1.PersonaId;

/**
 * v3 Identity (v1 package) Adapter - minimal helpers for v3+ identity primitives.
 *
 * <p>[Semantic] This adapter only deals with transport-level primitives (proto DTO ↔ raw bytes).
 * Runtime resolution / binding verification is implementation-defined and MUST NOT live in this repository.</p>
 *
 * @since 3.0.0
 */
public final class V3IdentityV1Adapter {

    private V3IdentityV1Adapter() {
        // [Index] NDS-JAVA-V3-IDENTITYV1-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * Create PersonaId from opaque bytes.
     *
     * @param value opaque bytes (must be non-empty)
     * @return PersonaId
     * @throws IllegalArgumentException if value is null/empty
     * @since 3.0.0
     */
    public static PersonaId createPersonaId(byte[] value) {
        if (value == null || value.length == 0) {
            throw new IllegalArgumentException("value must be non-empty");
        }
        return PersonaId.newBuilder()
            .setValue(com.google.protobuf.ByteString.copyFrom(value))
            .build();
    }

    /**
     * Extract opaque bytes from PersonaId.
     *
     * @param personaId PersonaId (non-null)
     * @return bytes
     * @since 3.0.0
     */
    public static byte[] toBytes(PersonaId personaId) {
        Objects.requireNonNull(personaId, "personaId");
        return personaId.getValue().toByteArray();
    }
}

