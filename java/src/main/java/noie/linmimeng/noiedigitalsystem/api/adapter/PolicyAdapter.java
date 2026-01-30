package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.policy.NdsPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Policy Adapter - NdsPolicy Domain ↔ Proto 轉換
 *
 * @since 2.2.0
 */
public final class PolicyAdapter {

    private PolicyAdapter() {
        // Utility class
    }

    public static noie.linmimeng.noiedigitalsystem.api.proto.policy.NdsPolicy toProto(NdsPolicy domain) {
        if (domain == null) return null;

        var builder = noie.linmimeng.noiedigitalsystem.api.proto.policy.NdsPolicy.newBuilder()
            .setPolicyId(domain.policyId())
            .setPolicyType(domain.policyType());

        if (domain.params() != null) {
            builder.putAllParams(domain.params());
        }
        if (domain.customConfig() != null && domain.customConfig().length > 0) {
            builder.setCustomConfig(com.google.protobuf.ByteString.copyFrom(domain.customConfig()));
        }
        if (domain.metadata() != null) {
            builder.putAllMetadata(domain.metadata());
        }

        return builder.build();
    }

    public static NdsPolicy fromProto(noie.linmimeng.noiedigitalsystem.api.proto.policy.NdsPolicy proto) {
        if (proto == null || proto.getPolicyId().isEmpty() || proto.getPolicyType().isEmpty()) {
            return null;
        }

        Map<String, String> params = new HashMap<>(proto.getParamsMap());
        Map<String, String> metadata = new HashMap<>(proto.getMetadataMap());
        byte[] customConfig = proto.getCustomConfig().isEmpty()
            ? new byte[0]
            : proto.getCustomConfig().toByteArray();

        return NdsPolicy.of(proto.getPolicyId(), proto.getPolicyType(), params, customConfig, metadata);
    }
}

