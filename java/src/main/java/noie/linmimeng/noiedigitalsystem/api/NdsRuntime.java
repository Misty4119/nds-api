package noie.linmimeng.noiedigitalsystem.api;

import noie.linmimeng.noiedigitalsystem.api.event.NdsEventBus;
import noie.linmimeng.noiedigitalsystem.api.projection.NdsQueryService;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentityService;
import noie.linmimeng.noiedigitalsystem.api.platform.NdsPlatform;
import java.util.concurrent.Executor;

/**
 * [Index] NDS-JAVA-RUNTIME-000
 * [Semantic] NDS runtime contract (provided by runtime modules such as nds-core).
 * 
 * <p>This is the core runtime interface for NDS.</p>
 * 
 * <p><b>Constitutional constraints:</b></p>
 * <ul>
 *   <li>[Constraint] All methods must be thread-safe.</li>
 *   <li>[Constraint] Methods must not block the calling thread.</li>
 *   <li>[Constraint] Must not return null (use empty collections / Optional where needed).</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public interface NdsRuntime {
    
    /**
     * Get the event bus.
     *
     * @return event bus instance (never null)
     */
    NdsEventBus eventBus();
    
    /**
     * Get the query service.
     *
     * @return query service instance (never null)
     */
    NdsQueryService query();
    
    /**
     * Get the identity service.
     *
     * @return identity service instance (never null)
     */
    NdsIdentityService identity();
    
    /**
     * Get runtime version.
     *
     * @return version string (MAJOR.MINOR.PATCH)
     */
    String version();
    
    /**
     * Check runtime health.
     *
     * @return true if healthy
     */
    boolean isHealthy();
    
    /**
     * Get platform abstraction.
     *
     * @return platform (never null)
     * @since 2.0.0
     */
    NdsPlatform platform();
    
    /**
     * Get the default executor (preferred).
     * 
     * <p><b>Important: async callback threading</b></p>
     * <p>Callbacks for {@code CompletableFuture} (e.g. thenAccept/thenApply) may run on non-main threads.
     * Different runtimes require different executors:</p>
     * <ul>
     *   <li>Minecraft: must use a main-thread executor (Bukkit API is not thread-safe)</li>
     *   <li>Spring Boot: prefer a Spring-managed executor</li>
     *   <li>Generic Java: default executor is acceptable</li>
     * </ul>
     * 
     * <p><b>Example:</b></p>
     * <pre>{@code
     * runtime.query().queryBalance(assetId, identity)
     *     .thenAcceptAsync(result -> {
     *         if (result.isSuccess()) {
     *             // [Index] NDS-JAVA-RUNTIME-EX-001 [Constraint] Minecraft: must run on the main thread (Bukkit API is not thread-safe).
     *             // [Index] NDS-JAVA-RUNTIME-EX-002 [Behavior] Spring Boot: runs on a Spring-managed executor.
     *             System.out.println("Balance: " + result.data());
     *         }
     *     }, runtime.defaultExecutor());
     * }</pre>
     * 
     * @return default executor (never null)
     * @since 2.0.0
     */
    default Executor defaultExecutor() {
        return platform().defaultExecutor();
    }
    
    /**
     * Get a main-thread executor (backward compatibility).
     * 
     * <p>This method is deprecated; prefer {@link #defaultExecutor()} for platform abstraction.</p>
     * 
     * @return executor (never null)
     * @deprecated Prefer {@link #defaultExecutor()} for platform abstraction.
     */
    @Deprecated(since = "2.0.0", forRemoval = false)
    default Executor mainThreadExecutor() {
        return defaultExecutor();
    }
}

