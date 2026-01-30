package noie.linmimeng.noiedigitalsystem.api.semantic;

import noie.linmimeng.noiedigitalsystem.api.asset.NdsAsset;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * NDS Semantic Service - 語義服務接口
 * 
 * <p>負責提供語義化功能。</p>
 * 
 * @since 2.0.0
 */
public interface NdsSemanticService {
    
    /**
     * 語義化資產
     * 
     * @param asset 資產對象
     * @return CompletableFuture 包含語義化結果
     */
    CompletableFuture<NdsResult<NdsSemantic>> semanticize(NdsAsset asset);
    
    /**
     * 語義化事件
     * 
     * @param event 事件對象
     * @return CompletableFuture 包含語義化結果
     */
    CompletableFuture<NdsResult<NdsSemantic>> semanticize(NdsEvent event);
    
    /**
     * 向量化數據
     * 
     * @param semantic 語義對象
     * @return CompletableFuture 包含向量化結果
     */
    CompletableFuture<NdsResult<NdsTensor>> vectorize(NdsSemantic semantic);
    
    /**
     * 語義搜索
     * 
     * @param query 查詢語義對象
     * @param limit 限制數量
     * @return CompletableFuture 包含搜索結果
     */
    CompletableFuture<NdsResult<List<NdsSemantic>>> search(NdsSemantic query, int limit);
}

