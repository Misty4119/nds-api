package noie.linmimeng.noiedigitalsystem.api.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * NDS Result - 結果模型（消滅 Exception）
 * 
 * <p>用於替代 Checked Exception，提供函數式錯誤處理。</p>
 * 
 * <p><b>憲法級規則：</b></p>
 * <ul>
 *   <li>API 層禁止 Checked Exception</li>
 *   <li>所有錯誤通過 NdsResult 表達</li>
 *   <li>支持函數式鏈式調用</li>
 * </ul>
 * 
 * @param <T> 成功時的數據類型
 * @since 2.0.0
 */
public interface NdsResult<T> {
    
    /**
     * 檢查是否成功
     * 
     * @return true 如果成功
     */
    boolean isSuccess();
    
    /**
     * 獲取數據
     * 
     * @return 數據（僅在成功時有效）
     * @throws IllegalStateException 如果結果不是成功
     */
    T data();
    
    /**
     * 獲取錯誤
     * 
     * @return 錯誤對象（僅在失敗時有效）
     * @throws IllegalStateException 如果結果不是失敗
     */
    NdsError error();
    
    /**
     * 映射成功值
     * 
     * @param mapper 映射函數
     * @param <R> 新類型
     * @return 新的結果
     */
    <R> NdsResult<R> map(Function<T, R> mapper);
    
    /**
     * 扁平映射成功值
     * 
     * @param mapper 映射函數（返回新的 Result）
     * @param <R> 新類型
     * @return 新的結果
     */
    <R> NdsResult<R> flatMap(Function<T, NdsResult<R>> mapper);
    
    /**
     * 處理成功情況
     * 
     * @param consumer 消費者
     * @return 當前結果
     */
    NdsResult<T> onSuccess(Consumer<T> consumer);
    
    /**
     * 處理失敗情況
     * 
     * @param consumer 消費者
     * @return 當前結果
     */
    NdsResult<T> onFailure(Consumer<NdsError> consumer);
    
    /**
     * 獲取 Optional 數據
     * 
     * @return Optional 數據
     */
    default Optional<T> toOptional() {
        return isSuccess() ? Optional.of(data()) : Optional.empty();
    }
    
    /**
     * 創建成功結果
     * 
     * @param data 數據
     * @param <T> 數據類型
     * @return 成功結果
     */
    static <T> NdsResult<T> success(T data) {
        return new NdsResultImpl<>(true, data, null);
    }
    
    /**
     * 創建失敗結果
     * 
     * @param error 錯誤對象
     * @param <T> 數據類型
     * @return 失敗結果
     */
    static <T> NdsResult<T> failure(NdsError error) {
        return new NdsResultImpl<>(false, null, error);
    }
    
    /**
     * 創建失敗結果（便捷方法）
     * 
     * @param code 錯誤代碼
     * @param message 錯誤消息
     * @param <T> 數據類型
     * @return 失敗結果
     */
    static <T> NdsResult<T> failure(String code, String message) {
        return failure(NdsError.of(code, message));
    }
}

