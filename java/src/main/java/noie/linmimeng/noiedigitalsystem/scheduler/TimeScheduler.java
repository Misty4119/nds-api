package noie.linmimeng.noiedigitalsystem.scheduler;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * [Index] NDS-JAVA-TIMESCHEDULER-000
 * [Semantic] Time-driven task scheduler with cron support.
 *
 * @since 2.0.0
 */
public interface TimeScheduler {

    /**
     * @return current system time adjusted to the configured timezone
     */
    ZonedDateTime getSystemTime();

    /**
     * Register a recurring task.
     *
     * @param id unique task ID
     * @param cron cron expression or shorthand (e.g. "daily", "weekly", "0 0 0 * * *", "14:30")
     * @param task callback receiving the scheduled execution time
     */
    void registerSchedule(String id, String cron, Consumer<ZonedDateTime> task);

    /**
     * Force-compute natural regeneration for a player's Digital key.
     * Typically called before GUI display to ensure a fresh value.
     *
     * @param playerUuid player UUID
     * @param key Digital key name (e.g. "stamina")
     */
    void calculateRegen(UUID playerUuid, String key);

    /**
     * @param taskId task ID
     * @return next scheduled execution time; empty if the task does not exist
     */
    Optional<ZonedDateTime> getNextExecutionTime(String taskId);

    /**
     * Force-trigger a task immediately (for testing only).
     *
     * @param taskId task ID
     * @return true if the task exists and was triggered; false otherwise
     */
    boolean triggerTask(String taskId);
}

