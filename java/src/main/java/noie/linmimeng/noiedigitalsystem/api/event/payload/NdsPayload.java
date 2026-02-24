package noie.linmimeng.noiedigitalsystem.api.event.payload;

import java.util.Map;
import java.util.Set;

/**
 * [Index] NDS-JAVA-PAYLOAD-000
 * [Semantic] Serializable event payload — enforces a closed set of allowed value types.
 *
 * <p>[Constraint] Allowed value types: String, Integer, Long, Double, Boolean, BigDecimal, BigInteger,
 * List&lt;String&gt;, Map&lt;String, Object&gt; (recursively checked), null.</p>
 * <p>[Constraint] Platform-specific or non-serializable objects are prohibited.
 * The runtime MUST reject events carrying prohibited types at build time.</p>
 *
 * @since 2.0.0
 */
public interface NdsPayload {

    /** @return String value for key; null if absent */
    String getString(String key);

    /** @return Integer value for key; null if absent */
    Integer getInt(String key);

    /** @return Long value for key; null if absent */
    Long getLong(String key);

    /** @return Double value for key; null if absent */
    Double getDouble(String key);

    /** @return Boolean value for key; null if absent */
    Boolean getBoolean(String key);

    /** @return BigDecimal value for key; null if absent */
    java.math.BigDecimal getBigDecimal(String key);

    /** @return string list for key; empty list if absent */
    java.util.List<String> getList(String key);

    /** @return nested map for key; empty map if absent */
    Map<String, Object> getMap(String key);

    /** @return raw map representation (for serialization) */
    Map<String, Object> toRawMap();

    /** @return true if key is present */
    boolean containsKey(String key);

    /** @return all keys in this payload */
    Set<String> keys();

    /** @return true if this payload contains no entries */
    boolean isEmpty();
}

