package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.result.NdsResult;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * [Index] NDS-JAVA-TRANSACTIONSERVICE-000
 * [Semantic] Transaction execution service contract.
 *
 * @since 2.0.0
 */
public interface NdsTransactionService {

    /**
     * @param transaction transaction event (must be valid)
     * @return async result; completes when the transaction is applied
     */
    CompletableFuture<NdsResult<Void>> execute(NdsTransaction transaction);

    /**
     * Convenience overload without an explicit NdsTransaction object.
     *
     * @param asset asset to modify
     * @param delta credit (positive) or debit (negative)
     * @param actor initiating identity
     * @param consistency consistency requirement
     * @return async result
     */
    CompletableFuture<NdsResult<Void>> execute(
        AssetId asset,
        BigDecimal delta,
        NdsIdentity actor,
        ConsistencyMode consistency
    );

    /**
     * Transfer amount from source to target (generates debit + credit transactions).
     *
     * @param asset asset to transfer
     * @param amount transfer amount (must be positive)
     * @param source debited identity
     * @param target credited identity
     * @param reason optional human-readable reason (nullable)
     * @return async result
     */
    CompletableFuture<NdsResult<Void>> transfer(
        AssetId asset,
        BigDecimal amount,
        NdsIdentity source,
        NdsIdentity target,
        String reason
    );
}

