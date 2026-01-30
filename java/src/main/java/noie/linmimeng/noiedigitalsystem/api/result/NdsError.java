package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Map;

/**
 * [Index] NDS-JAVA-ERROR-000
 * [Semantic] Structured error object.
 * 
 * @since 2.0.0
 */
public interface NdsError {
    
    /**
     * @return machine-readable error code (e.g. "INSUFFICIENT_BALANCE", "ASSET_NOT_FOUND")
     */
    String code();
    
    /**
     * @return human-readable message
     */
    String message();
    
    /**
     * @return structured details map (may be empty; never null)
     */
    Map<String, Object> details();
    
    /**
     * @return original exception (may be null)
     */
    Throwable cause();
    
    /**
     * Create an error.
     *
     * @param code error code
     * @param message error message
     * @return error object
     */
    static NdsError of(String code, String message) {
        return new NdsErrorImpl(code, message, Map.of(), null);
    }
    
    /**
     * Create an error with details.
     *
     * @param code error code
     * @param message error message
     * @param details structured details
     * @return error object
     */
    static NdsError of(String code, String message, Map<String, Object> details) {
        return new NdsErrorImpl(code, message, details, null);
    }
    
    /**
     * Create an error with a cause.
     *
     * @param code error code
     * @param message error message
     * @param cause original exception
     * @return error object
     */
    static NdsError of(String code, String message, Throwable cause) {
        return new NdsErrorImpl(code, message, Map.of(), cause);
    }
}

