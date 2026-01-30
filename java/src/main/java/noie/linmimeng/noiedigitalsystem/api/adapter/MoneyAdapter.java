package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import noie.linmimeng.noiedigitalsystem.api.proto.ledger.v1.Money;

/**
 * Money Adapter - BigDecimal ↔ Proto Money（fixed-point: units + nanos/1e9）
 *
 * <p><b>v3+</b>：ledger 熱路徑金額使用 fixed-point {@link Money} 表示，以避免高頻 Decimal(string) parse。</p>
 *
 * <p>規格（normative）：見 {@code spec/docs/V3.md}。</p>
 *
 * @since 3.0.0
 */
public final class MoneyAdapter {

    private static final BigInteger NANOS_DIVISOR = BigInteger.valueOf(1_000_000_000L);
    private static final int NANOS_SCALE = 9;

    private MoneyAdapter() {
        // [Index] NDS-JAVA-MONEY-001 [Constraint] Utility class; instantiation is prohibited.
    }

    /**
     * Convert BigDecimal to proto Money (exact; no rounding).
     *
     * <p>Rule: if fractional digits &gt; 9 after stripping trailing zeros, this method MUST fail.</p>
     *
     * @param currencyCode currency identifier (must be non-empty)
     * @param amount BigDecimal amount (nullable)
     * @return proto Money; returns null if amount is null
     * @throws IllegalArgumentException if currencyCode is blank or amount has &gt; 9 fractional digits
     */
    public static Money toProto(String currencyCode, BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("currencyCode must be non-empty");
        }

        BigDecimal normalized = amount.stripTrailingZeros();
        if (normalized.scale() > NANOS_SCALE) {
            throw new IllegalArgumentException("amount has more than 9 fractional digits (exact conversion required)");
        }

        if (normalized.signum() == 0) {
            return Money.newBuilder()
                .setCurrencyCode(currencyCode)
                .setUnits(0L)
                .setNanos(0)
                .build();
        }

        BigDecimal scaled = normalized.setScale(NANOS_SCALE);
        int sign = scaled.signum();
        BigInteger unscaledAbs = scaled.unscaledValue().abs();
        BigInteger[] divRem = unscaledAbs.divideAndRemainder(NANOS_DIVISOR);

        long unitsAbs = divRem[0].longValueExact();
        int nanosAbs = divRem[1].intValueExact();

        long units = sign < 0 ? -unitsAbs : unitsAbs;
        int nanos = sign < 0 ? -nanosAbs : nanosAbs;

        return Money.newBuilder()
            .setCurrencyCode(currencyCode)
            .setUnits(units)
            .setNanos(nanos)
            .build();
    }

    /**
     * Convert proto Money to BigDecimal (exact).
     *
     * @param proto proto Money (nullable)
     * @return BigDecimal; returns null if proto is null
     * @throws IllegalArgumentException if nanos is out of range or sign encoding is inconsistent
     */
    public static BigDecimal fromProto(Money proto) {
        if (proto == null) {
            return null;
        }

        if (proto.getCurrencyCode() == null || proto.getCurrencyCode().isBlank()) {
            throw new IllegalArgumentException("currencyCode must be non-empty");
        }

        long units = proto.getUnits();
        int nanos = proto.getNanos();

        if (Math.abs((long) nanos) >= 1_000_000_000L) {
            throw new IllegalArgumentException("nanos out of range: " + nanos);
        }

        // Reject ambiguous sign encodings such as units<0 with nanos>0 (and vice versa).
        if (units > 0 && nanos < 0) {
            throw new IllegalArgumentException("inconsistent sign: units>0 but nanos<0");
        }
        if (units < 0 && nanos > 0) {
            throw new IllegalArgumentException("inconsistent sign: units<0 but nanos>0");
        }

        if (units == 0L && nanos == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal unitsBd = BigDecimal.valueOf(units);
        BigDecimal nanosBd = BigDecimal.valueOf(nanos, NANOS_SCALE);
        return unitsBd.add(nanosBd);
    }
}

