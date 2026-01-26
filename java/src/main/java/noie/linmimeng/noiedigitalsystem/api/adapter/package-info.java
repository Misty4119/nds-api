/**
 * NDS Adapter 包 - Proto ↔ Domain 類型轉換
 * 
 * <p>本包提供 Protocol Buffers 生成類型與 NDS Domain 類型之間的雙向轉換。</p>
 * 
 * <h2>設計原則</h2>
 * <ul>
 *   <li><b>向後兼容</b>：現有 API 不變，Adapter 作為內部實現</li>
 *   <li><b>雙向轉換</b>：支持 Domain → Proto 和 Proto → Domain</li>
 *   <li><b>Null 安全</b>：正確處理 null 值</li>
 *   <li><b>高精度</b>：BigDecimal ↔ Decimal 精確轉換</li>
 * </ul>
 * 
 * <h2>使用方式</h2>
 * <pre>{@code
 * // Domain → Proto
 * NdsIdentity domainIdentity = NdsIdentity.of("player-uuid", IdentityType.PLAYER);
 * nds.identity.NdsIdentity protoIdentity = IdentityAdapter.toProto(domainIdentity);
 * 
 * // Proto → Domain
 * NdsIdentity backToDomain = IdentityAdapter.fromProto(protoIdentity);
 * }</pre>
 * 
 * @since 2.0.0
 */
package noie.linmimeng.noiedigitalsystem.api.adapter;
