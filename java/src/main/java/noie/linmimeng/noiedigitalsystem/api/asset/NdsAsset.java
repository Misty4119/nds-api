package noie.linmimeng.noiedigitalsystem.api.asset;

import java.util.Map;

/**
 * [Index] NDS-JAVA-ASSET-000
 * [Semantic] Asset definition model (currency, item, reputation, etc.).
 *
 * <p>[Constraint] Asset does not hold numeric values; balances are managed by transactions.</p>
 * <p>[Behavior] Tags serve as semantic entry points for AI queries, UI classification, and vectorization.</p>
 *
 * @since 2.0.0
 */
public interface NdsAsset {

    /** @return asset ID */
    AssetId assetId();

    /** @return ownership scope */
    AssetScope scope();

    /**
     * @return tag map for semantic queries and UI classification (never null; may be empty)
     */
    Map<String, String> tags();

    /** @return supplementary metadata (never null; may be empty) */
    Map<String, String> metadata();

    /** @return true if assetId and scope are both non-null */
    default boolean isValid() {
        return assetId() != null && scope() != null;
    }

    /**
     * @param key tag key
     * @param value tag value; null = check key presence only
     * @return true if the tag key (and optionally value) matches
     */
    default boolean hasTag(String key, String value) {
        if (key == null) return false;
        String tagValue = tags().get(key);
        if (value == null) {
            return tagValue != null;
        }
        return value.equals(tagValue);
    }
}

