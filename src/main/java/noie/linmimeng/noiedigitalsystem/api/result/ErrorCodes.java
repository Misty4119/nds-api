package noie.linmimeng.noiedigitalsystem.api.result;

/**
 * 常見錯誤代碼常量
 * 
 * @since 2.0.0
 */
public final class ErrorCodes {
    // 資產相關
    public static final String ASSET_NOT_FOUND = "ASSET_NOT_FOUND";
    public static final String ASSET_ALREADY_EXISTS = "ASSET_ALREADY_EXISTS";
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT_BALANCE";
    public static final String ASSET_LIMIT_EXCEEDED = "ASSET_LIMIT_EXCEEDED";
    
    // 身份相關
    public static final String IDENTITY_NOT_FOUND = "IDENTITY_NOT_FOUND";
    public static final String IDENTITY_INVALID = "IDENTITY_INVALID";
    
    // 交易相關
    public static final String TRANSACTION_FAILED = "TRANSACTION_FAILED";
    public static final String TRANSACTION_ROLLED_BACK = "TRANSACTION_ROLLED_BACK";
    public static final String CONSISTENCY_VIOLATION = "CONSISTENCY_VIOLATION";
    
    // 系統相關
    public static final String SYSTEM_NOT_INITIALIZED = "SYSTEM_NOT_INITIALIZED";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String TIMEOUT = "TIMEOUT";
    
    private ErrorCodes() {
        // 工具類，禁止實例化
    }
}

