package noie.linmimeng.noiedigitalsystem.api.extension;

import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-EXTENSIONREGISTRY-000
 * [Semantic] Registry for managing NdsExtension lifecycle.
 *
 * @since 2.0.0
 */
public interface NdsExtensionRegistry {

    /**
     * @param extension extension instance to register
     * @return async registration result
     */
    CompletableFuture<NdsResult<Void>> register(NdsExtension extension);

    /**
     * @param namespace namespace of the extension to unregister
     * @return async unregistration result
     */
    CompletableFuture<NdsResult<Void>> unregister(String namespace);

    /** @return async result containing all registered extensions */
    CompletableFuture<NdsResult<List<NdsExtension>>> list();

    /**
     * @param namespace namespace to look up
     * @return async result containing the matching extension
     */
    CompletableFuture<NdsResult<NdsExtension>> get(String namespace);
}

