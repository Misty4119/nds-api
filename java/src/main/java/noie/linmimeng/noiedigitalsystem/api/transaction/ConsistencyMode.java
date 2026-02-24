package noie.linmimeng.noiedigitalsystem.api.transaction;

/**
 * [Index] NDS-JAVA-CONSISTENCYMODE-000
 * [Semantic] Consistency requirements for applying a transaction.
 *
 * @since 2.0.0
 */
public enum ConsistencyMode {
    /**
     * Strong consistency.
     * Applies immediately; prioritizes correctness.
     * Suitable for critical economic operations (payments, transfers).
     */
    STRONG,

    /**
     * Eventual consistency.
     * May apply with delay; converges over time.
     * Suitable for non-critical operations (stats, logs).
     */
    EVENTUAL,

    /**
     * Optimistic consistency.
     * Based on optimistic concurrency; may fail.
     * Suitable for high-concurrency scenarios.
     */
    OPTIMISTIC;

    /**
     * @param str string token (case-insensitive)
     * @return parsed mode; defaults to {@link #STRONG} if null or unrecognized
     */
    public static ConsistencyMode fromString(String str) {
        if (str == null) return STRONG;
        try {
            return valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STRONG;
        }
    }
}

