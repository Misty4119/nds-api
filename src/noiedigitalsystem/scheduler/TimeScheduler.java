package noie.linmimeng.noiedigitalsystem.scheduler;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 時間排程器接口
 * 提供時間驅動任務的註冊和管理功能
 */
public interface TimeScheduler {
    
    /**
     * 獲取 NDS 系統當前時間（已校正 Config 時區）
     * @return 當前時間的 ZonedDateTime
     */
    ZonedDateTime getSystemTime();
    
    /**
     * 註冊一個自定義週期任務
     * @param id 任務ID（必須唯一）
     * @param cron Cron 表達式或自然語言（如 "daily", "weekly", "0 0 0 * * *", "14:30"）
     * @param task 執行的邏輯，接收執行時間作為參數
     */
    void registerSchedule(String id, String cron, Consumer<ZonedDateTime> task);
    
    /**
     * 強制計算某玩家的自然回復（通常用於 GUI 顯示前）
     * @param playerUuid 玩家 UUID
     * @param key Digital 鍵名（如 "stamina"）
     */
    void calculateRegen(UUID playerUuid, String key);
    
    /**
     * 獲取指定任務的下一次執行時間
     * @param taskId 任務ID
     * @return 下一次執行時間，如果任務不存在則返回 empty
     */
    Optional<ZonedDateTime> getNextExecutionTime(String taskId);
    
    /**
     * 強制立即觸發一次任務（用於測試）
     * @param taskId 任務ID
     * @return 如果任務存在並成功觸發返回 true，否則返回 false
     */
    boolean triggerTask(String taskId);
}

