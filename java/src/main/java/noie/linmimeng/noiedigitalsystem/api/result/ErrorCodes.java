package noie.linmimeng.noiedigitalsystem.api.result;

/**
 * [Index] NDS-JAVA-ERRORCODES-000
 * [Semantic] Common error code constants.
 * 
 * @since 2.0.0
 */
public final class ErrorCodes {
    // [Index] NDS-JAVA-ERRORCODES-010 [Semantic] Asset-related errors.
    public static final String ASSET_NOT_FOUND = "ASSET_NOT_FOUND";
    public static final String ASSET_ALREADY_EXISTS = "ASSET_ALREADY_EXISTS";
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT_BALANCE";
    public static final String ASSET_LIMIT_EXCEEDED = "ASSET_LIMIT_EXCEEDED";
    
    // [Index] NDS-JAVA-ERRORCODES-020 [Semantic] Identity-related errors.
    public static final String IDENTITY_NOT_FOUND = "IDENTITY_NOT_FOUND";
    public static final String IDENTITY_INVALID = "IDENTITY_INVALID";
    
    // [Index] NDS-JAVA-ERRORCODES-030 [Semantic] Transaction-related errors.
    public static final String TRANSACTION_FAILED = "TRANSACTION_FAILED";
    public static final String TRANSACTION_ROLLED_BACK = "TRANSACTION_ROLLED_BACK";
    public static final String CONSISTENCY_VIOLATION = "CONSISTENCY_VIOLATION";
    
    // [Index] NDS-JAVA-ERRORCODES-040 [Semantic] System-related errors.
    public static final String SYSTEM_NOT_INITIALIZED = "SYSTEM_NOT_INITIALIZED";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String TIMEOUT = "TIMEOUT";
    
    private ErrorCodes() {
        // [Index] NDS-JAVA-ERRORCODES-001 [Constraint] Utility class; instantiation is prohibited.
    }
}

