package noie.linmimeng.noiedigitalsystem.api.result;

/**
 * [Index] NDS-JAVA-RESULTS-000
 * [Semantic] Result utility constants.
 * 
 * <p>Provides commonly used result constants to avoid repeating boilerplate (e.g. {@code NdsResult.success(null)}).</p>
 * 
 * @since 2.0.0
 */
public final class NdsResults {
    
    /**
     * Success constant for {@code Void} results.
     * 
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // [Index] NDS-JAVA-RESULTS-EX-001 [Behavior] Preferred usage.
     * return CompletableFuture.completedFuture(NdsResults.OK);
     * 
     * // [Index] NDS-JAVA-RESULTS-EX-002 [Behavior] Avoid repeating success(null) boilerplate.
     * return CompletableFuture.completedFuture(NdsResult.success(null));
     * }</pre>
     */
    public static final NdsResult<Void> OK = NdsResult.success(null);
    
    private NdsResults() {
        // [Index] NDS-JAVA-RESULTS-001 [Constraint] Utility class; instantiation is prohibited.
    }
}

