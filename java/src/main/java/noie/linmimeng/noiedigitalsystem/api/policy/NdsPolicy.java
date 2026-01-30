package noie.linmimeng.noiedigitalsystem.api.policy;

import java.util.Map;

/**
 * [Index] NDS-JAVA-POLICY-000
 * [Semantic] Portable policy descriptor (data only).
 *
 * <p>[Behavior] This is a protocol-facing value object; evaluation semantics are implementation-defined.</p>
 *
 * @since 2.2.0
 */
public interface NdsPolicy {

    /**
     * Policy unique id.
     */
    String policyId();

    /**
     * Policy type tag (implementation-defined).
     */
    String policyType();

    /**
     * Parameters (protocol does not define keys).
     */
    Map<String, String> params();

    /**
     * Custom config blob (arbitrary bytes).
     */
    byte[] customConfig();

    /**
     * Metadata (implementation-defined).
     */
    Map<String, String> metadata();

    /**
     * Lightweight factory (no IO).
     */
    static NdsPolicy of(
        String policyId,
        String policyType,
        Map<String, String> params,
        byte[] customConfig,
        Map<String, String> metadata
    ) {
        return new NdsPolicyImpl(policyId, policyType, params, customConfig, metadata);
    }
}

