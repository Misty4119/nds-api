/**
 * [Index] NDS-JAVA-ADAPTER-PKG-000
 * [Semantic] Proto ↔ Domain bidirectional type conversions.
 *
 * <p>[Constraint] Additive-only changes; null-safe inputs; BigDecimal precision invariant.</p>
 *
 * <pre>{@code
 * NdsIdentity domain = NdsIdentity.of("player-uuid", IdentityType.PLAYER);
 * nds.identity.NdsIdentity proto = IdentityAdapter.toProto(domain);
 * NdsIdentity back = IdentityAdapter.fromProto(proto);
 * }</pre>
 *
 * @since 2.0.0
 */
package noie.linmimeng.noiedigitalsystem.api.adapter;
