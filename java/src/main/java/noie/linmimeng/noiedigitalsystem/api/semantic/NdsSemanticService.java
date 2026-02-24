package noie.linmimeng.noiedigitalsystem.api.semantic;

import noie.linmimeng.noiedigitalsystem.api.asset.NdsAsset;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-SEMANTICSERVICE-000
 * [Semantic] Service for semantic extraction, vectorization, and semantic search.
 *
 * @since 2.0.0
 */
public interface NdsSemanticService {

    /**
     * @param asset asset to semanticize
     * @return async result containing the extracted NdsSemantic
     */
    CompletableFuture<NdsResult<NdsSemantic>> semanticize(NdsAsset asset);

    /**
     * @param event event to semanticize
     * @return async result containing the extracted NdsSemantic
     */
    CompletableFuture<NdsResult<NdsSemantic>> semanticize(NdsEvent event);

    /**
     * @param semantic semantic object to vectorize
     * @return async result containing the NdsTensor embedding
     */
    CompletableFuture<NdsResult<NdsTensor>> vectorize(NdsSemantic semantic);

    /**
     * @param query query semantic object
     * @param limit maximum number of results
     * @return async result containing matched NdsSemantic objects by similarity
     */
    CompletableFuture<NdsResult<List<NdsSemantic>>> search(NdsSemantic query, int limit);
}

