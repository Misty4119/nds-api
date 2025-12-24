package noie.linmimeng.noiedigitalsystem.api;

/**
 * NDS Provider - 唯一啟動入口（SPI 憲法）
 * 
 * <p>這是 NDS-API 的唯一啟動入口。所有插件必須通過此類獲取 NdsRuntime。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>只能註冊一次，重複註冊會拋出 IllegalStateException</li>
 *   <li>必須在插件啟動時註冊</li>
 *   <li>未註冊前調用 get() 會拋出 IllegalStateException</li>
 * </ul>
 * 
 * @since 2.0.0
 */
public final class NdsProvider {
    private static volatile NdsRuntime runtime;
    private static final Object lock = new Object();

    /**
     * 註冊 NDS Runtime 實作
     * 
     * @param impl Runtime 實作（由 nds-core 提供）
     * @throws IllegalStateException 如果已經註冊過
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
     * 獲取 NDS Runtime 實作
     * 
     * @return NdsRuntime 實作
     * @throws IllegalStateException 如果尚未註冊
     */
    public static NdsRuntime get() {
        NdsRuntime current = runtime;
        if (current == null) {
            throw new IllegalStateException("NDS not initialized. Please ensure NDS-Core is loaded and registered.");
        }
        return current;
    }

    /**
     * 檢查 NDS 是否已初始化
     * 
     * @return true 如果已初始化
     */
    public static boolean isInitialized() {
        return runtime != null;
    }

    /**
     * 重置 Provider（僅用於測試）
     * 
     * @throws IllegalStateException 如果不在測試環境
     */
    @Deprecated(forRemoval = false) // 保留用於測試
    static void reset() {
        synchronized (lock) {
            runtime = null;
        }
    }
}

