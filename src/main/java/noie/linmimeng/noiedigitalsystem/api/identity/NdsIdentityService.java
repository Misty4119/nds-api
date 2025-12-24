package noie.linmimeng.noiedigitalsystem.api.identity;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Identity Service - 身份服務接口（完整定義）
 * 
 * @since 2.0.0
 */
public interface NdsIdentityService {
    
    /**
     * 解析身份
     * 
     * @param id 身份 ID
     * @return CompletableFuture 包含身份結果
     */
    CompletableFuture<NdsResult<NdsIdentity>> resolve(String id);
    
    /**
     * 驗證身份
     * 
     * @param identity 身份對象
     * @return CompletableFuture 包含驗證結果
     */
    CompletableFuture<NdsResult<Boolean>> verify(NdsIdentity identity);
    
    /**
     * 創建身份
     * 
     * @param id 身份 ID
     * @param type 身份類型
     * @param metadata 元數據（可為 null）
     * @return CompletableFuture 包含創建的身份
     */
    CompletableFuture<NdsResult<NdsIdentity>> create(String id, IdentityType type, 
                                                       Map<String, String> metadata);
    
    /**
     * 更新身份元數據
     * 
     * @param identity 身份對象
     * @param metadata 新的元數據（可為 null）
     * @return CompletableFuture 包含更新後的身份
     */
    CompletableFuture<NdsResult<NdsIdentity>> updateMetadata(NdsIdentity identity, 
                                                              Map<String, String> metadata);
    
    /**
     * 查詢身份（根據類型）
     * 
     * @param type 身份類型
     * @param limit 限制數量
     * @param offset 偏移量
     * @return CompletableFuture 包含身份列表
     */
    CompletableFuture<NdsResult<List<NdsIdentity>>> query(IdentityType type, int limit, int offset);
}

