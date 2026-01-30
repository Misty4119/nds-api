package noie.linmimeng.noiedigitalsystem.api;

/**
 * [Index] NDS-JAVA-PROVIDER-000
 * [Semantic] NDS runtime provider (single entry point).
 * 
 * <p>This is the only entry point for acquiring {@link NdsRuntime}.</p>
 * 
 * <p><b>Constitutional constraints:</b></p>
 * <ul>
 *   <li>[Constraint] Register exactly once; re-registration throws {@link IllegalStateException}.</li>
 *   <li>[Constraint] Must be registered during runtime bootstrap.</li>
 *   <li>[Constraint] Calling {@link #get()} before registration throws {@link IllegalStateException}.</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public final class NdsProvider {
    private static volatile NdsRuntime runtime;
    private static final Object lock = new Object();

    /**
     * Register an {@link NdsRuntime} implementation.
     * 
     * @param impl runtime implementation (provided by runtime modules such as nds-core)
     * @throws IllegalStateException if already registered
     */
    public static synchronized void register(NdsRuntime impl) {
        if (impl == null) {
            throw new IllegalArgumentException("NdsRuntime cannot be null");
        }
        synchronized (lock) {
            if (runtime != null) {
                throw new IllegalStateException("NDS already initialized. Cannot register twice.");
            }
            runtime = impl;
        }
    }

    /**
     * Get the registered {@link NdsRuntime}.
     * 
     * @return registered runtime
     * @throws IllegalStateException if not registered
     */
    public static NdsRuntime get() {
        NdsRuntime current = runtime;
        if (current == null) {
            throw new IllegalStateException("NDS not initialized. Please ensure NDS-Core is loaded and registered.");
        }
        return current;
    }

    /**
     * Check whether the runtime is initialized.
     * 
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return runtime != null;
    }

    /**
     * Reset provider (tests only).
     */
    @Deprecated(forRemoval = false) // [Index] NDS-JAVA-PROVIDER-001 [Trace] Retained for tests.
    static void reset() {
        synchronized (lock) {
            runtime = null;
        }
    }
}

