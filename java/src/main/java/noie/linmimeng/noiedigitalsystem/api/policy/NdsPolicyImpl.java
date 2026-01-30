package noie.linmimeng.noiedigitalsystem.api.policy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * [Index] NDS-JAVA-POLICY-IMPL-000
 * [Semantic] Default immutable {@link NdsPolicy} implementation.
 *
 * @since 2.2.0
 */
final class NdsPolicyImpl implements NdsPolicy {
    private final String policyId;
    private final String policyType;
    private final Map<String, String> params;
    private final byte[] customConfig;
    private final Map<String, String> metadata;

    NdsPolicyImpl(
        String policyId,
        String policyType,
        Map<String, String> params,
        byte[] customConfig,
        Map<String, String> metadata
    ) {
        if (policyId == null || policyId.isEmpty()) {
            throw new IllegalArgumentException("policyId cannot be null or empty");
        }
        if (policyType == null || policyType.isEmpty()) {
            throw new IllegalArgumentException("policyType cannot be null or empty");
        }
        this.policyId = policyId;
        this.policyType = policyType;
        this.params = params != null ? Collections.unmodifiableMap(new HashMap<>(params)) : Map.of();
        this.customConfig = customConfig != null ? customConfig.clone() : new byte[0];
        this.metadata = metadata != null ? Collections.unmodifiableMap(new HashMap<>(metadata)) : Map.of();
    }

    @Override
    public String policyId() {
        return policyId;
    }

    @Override
    public String policyType() {
        return policyType;
    }

    @Override
    public Map<String, String> params() {
        return params;
    }

    @Override
    public byte[] customConfig() {
        return customConfig.clone();
    }

    @Override
    public Map<String, String> metadata() {
        return metadata;
    }
}

