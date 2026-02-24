package noie.linmimeng.noiedigitalsystem.api.platform;

import java.util.concurrent.Executor;

/**
 * [Index] NDS-JAVA-PLATFORM-000
 * [Semantic] Platform abstraction for runtime-specific executor selection.
 *
 * <p>[Behavior] Minecraft: main-thread executor (Bukkit API is not thread-safe).
 * Spring Boot: Spring-managed executor. Generic Java: ForkJoinPool.commonPool().</p>
 *
 * @since 2.0.0
 */
public interface NdsPlatform {

    /**
     * @return default executor for async callbacks (never null)
     */
    Executor defaultExecutor();

    /**
     * @return human-readable platform name (e.g. "Minecraft", "Spring Boot", "Standard Java")
     */
    String platformName();

    /** @return true if running on a Minecraft platform */
    default boolean isMinecraft() {
        return false;
    }
}

