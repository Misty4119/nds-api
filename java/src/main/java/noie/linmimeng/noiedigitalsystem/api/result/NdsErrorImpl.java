package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * [Index] NDS-JAVA-ERROR-IMPL-000
 * [Semantic] Default implementation of {@link NdsError}.
 * 
 * @since 2.0.0
 */
final class NdsErrorImpl implements NdsError {
    private final String code;
    private final String message;
    private final Map<String, Object> details;
    private final Throwable cause;
    
    NdsErrorImpl(String code, String message, Map<String, Object> details, Throwable cause) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Error code cannot be null or empty");
        }
        if (message == null) {
            throw new IllegalArgumentException("Error message cannot be null");
        }
        this.code = code;
        this.message = message;
        this.details = details != null ? Collections.unmodifiableMap(new HashMap<>(details)) : Map.of();
        this.cause = cause;
    }
    
    @Override
    public String code() {
        return code;
    }
    
    @Override
    public String message() {
        return message;
    }
    
    @Override
    public Map<String, Object> details() {
        return details;
    }
    
    @Override
    public Throwable cause() {
        return cause;
    }
    
    @Override
    public String toString() {
        return "NdsError{code='" + code + "', message='" + message + "'}";
    }
}

