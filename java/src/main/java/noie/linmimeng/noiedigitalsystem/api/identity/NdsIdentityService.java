package noie.linmimeng.noiedigitalsystem.api.identity;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-IDENTITYSERVICE-000
 * [Semantic] Identity lifecycle management contract.
 *
 * @since 2.0.0
 */
public interface NdsIdentityService {

    /**
     * @param id identity ID to resolve
     * @return async result containing the resolved identity
     */
    CompletableFuture<NdsResult<NdsIdentity>> resolve(String id);

    /**
     * @param identity identity to verify
     * @return async result: true if identity is valid and recognized
     */
    CompletableFuture<NdsResult<Boolean>> verify(NdsIdentity identity);

    /**
     * @param id identity ID
     * @param type identity type
     * @param metadata supplementary metadata (nullable)
     * @return async result containing the created identity
     */
    CompletableFuture<NdsResult<NdsIdentity>> create(String id, IdentityType type,
                                                      Map<String, String> metadata);

    /**
     * @param identity identity to update
     * @param metadata replacement metadata (nullable = no change)
     * @return async result containing the updated identity
     */
    CompletableFuture<NdsResult<NdsIdentity>> updateMetadata(NdsIdentity identity,
                                                             Map<String, String> metadata);

    /**
     * @param type filter by identity type
     * @param limit maximum result count
     * @param offset pagination offset
     * @return async result containing matched identities
     */
    CompletableFuture<NdsResult<List<NdsIdentity>>> query(IdentityType type, int limit, int offset);
}

