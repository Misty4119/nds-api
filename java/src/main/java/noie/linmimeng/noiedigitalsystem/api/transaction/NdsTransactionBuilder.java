package noie.linmimeng.noiedigitalsystem.api.transaction;

import noie.linmimeng.noiedigitalsystem.api.asset.AssetId;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * [Index] NDS-JAVA-TRANSACTIONBUILDER-000
 * [Semantic] Builder for immutable {@link NdsTransaction} instances.
 *
 * <p><b>Important: avoid the polymorphism trap</b></p>
 * <p>{@link NdsTransaction} is a specialized event type. Do not use
 * {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEventBuilder} for transactions, because
 * it produces {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEvent} and cannot be safely cast to
 * {@link NdsTransaction}. Use {@link NdsTransactionBuilder} instead.</p>
 * 
 * @since 2.0.0
 */
public interface NdsTransactionBuilder {
    
    /**
     * @param id transaction ID; auto-generated if null
     * @return this builder
     */
    NdsTransactionBuilder id(EventId id);

    /**
     * @param occurredAt event timestamp; defaults to current time if null
     * @return this builder
     */
    NdsTransactionBuilder occurredAt(Instant occurredAt);

    /**
     * @param actor initiating identity (required)
     * @return this builder
     */
    NdsTransactionBuilder actor(NdsIdentity actor);

    /**
     * @param asset asset subject to the delta (required)
     * @return this builder
     */
    NdsTransactionBuilder asset(AssetId asset);

    /**
     * @param delta amount change: positive = credit, negative = debit (required)
     * @return this builder
     */
    NdsTransactionBuilder delta(BigDecimal delta);

    /**
     * @param consistency consistency requirement (required)
     * @return this builder
     */
    NdsTransactionBuilder consistency(ConsistencyMode consistency);

    /**
     * @param source debited identity for transfers (optional)
     * @return this builder
     */
    NdsTransactionBuilder source(NdsIdentity source);

    /**
     * @param target credited identity for transfers (optional)
     * @return this builder
     */
    NdsTransactionBuilder target(NdsIdentity target);

    /**
     * @param reason human-readable reason (optional)
     * @return this builder
     */
    NdsTransactionBuilder reason(String reason);

    /**
     * @param schemaVersion payload schema version (default: 1)
     * @return this builder
     */
    NdsTransactionBuilder schemaVersion(int schemaVersion);

    /**
     * @param metadata additional key-value metadata (nullable)
     * @return this builder
     */
    NdsTransactionBuilder metadata(Map<String, String> metadata);

    /**
     * Build the immutable transaction event.
     *
     * <p>[Behavior] Payload is auto-constructed from: asset (fullId), delta, consistency, source, target, reason.</p>
     * <p>[Constraint] Payload values undergo runtime type validation (see {@link noie.linmimeng.noiedigitalsystem.api.event.NdsEventBuilder#build()}).</p>
     *
     * @return immutable NdsTransaction
     * @throws IllegalStateException if required fields are missing
     * @throws IllegalArgumentException if payload contains prohibited types
     */
    NdsTransaction build();

    /** @return new NdsTransactionBuilder (implementation provided by nds-core) */
    static NdsTransactionBuilder create() {
        // [Index] NDS-JAVA-TRANSACTIONBUILDER-900 [Trace] Implementation is provided by nds-core.
        throw new UnsupportedOperationException("NdsTransactionBuilder implementation must be provided by nds-core");
    }
}

