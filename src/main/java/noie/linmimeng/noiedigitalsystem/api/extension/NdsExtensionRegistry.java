package noie.linmimeng.noiedigitalsystem.api.extension;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Extension Registry - 擴展註冊表
 * 
 * <p>負責管理擴展的註冊和執行。</p>
 * 
 * @since 2.0.0
 */
public interface NdsExtensionRegistry {
    
    /**
     * 註冊擴展
     * 
     * @param extension 擴展實例
     * @return CompletableFuture 包含註冊結果
     */
    CompletableFuture<NdsResult<Void>> register(NdsExtension extension);
    
    /**
     * 取消註冊擴展
     * 
     * @param namespace 命名空間
     * @return CompletableFuture 包含取消註冊結果
     */
    CompletableFuture<NdsResult<Void>> unregister(String namespace);
    
    /**
     * 獲取擴展列表
     * 
     * @return CompletableFuture 包含擴展列表
     */
    CompletableFuture<NdsResult<List<NdsExtension>>> list();
    
    /**
     * 獲取指定命名空間的擴展
     * 
     * @param namespace 命名空間
     * @return CompletableFuture 包含擴展結果
     */
    CompletableFuture<NdsResult<NdsExtension>> get(String namespace);
}

