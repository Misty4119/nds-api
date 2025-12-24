package noie.linmimeng.noiedigitalsystem.api.result;

/**
 * NDS Results - 結果工具類
 * 
 * <p>提供常用的結果常量，避免代碼中充斥 {@code NdsResult.success(null)} 這種寫法。</p>
 * 
 * @since 2.0.0
 */
public final class NdsResults {
    
    /**
     * 成功結果常量（用於 Void 類型）
     * 
     * <p><b>使用範例：</b></p>
     * <pre>{@code
     * // 優雅寫法
     * return CompletableFuture.completedFuture(NdsResults.OK);
     * 
     * // 而不是
     * return CompletableFuture.completedFuture(NdsResult.success(null));
     * }</pre>
     */
    public static final NdsResult<Void> OK = NdsResult.success(null);
    
    private NdsResults() {
        // 工具類，禁止實例化
    }
}

