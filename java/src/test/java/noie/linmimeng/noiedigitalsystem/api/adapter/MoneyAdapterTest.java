package noie.linmimeng.noiedigitalsystem.api.adapter;

import java.math.BigDecimal;
import noie.linmimeng.noiedigitalsystem.api.proto.ledger.v1.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyAdapterTest {

    @Test
    void toProto_shouldConvertExactAmount() {
        Money money = MoneyAdapter.toProto("NDS", new BigDecimal("100.5"));
        assertNotNull(money);
        assertEquals("NDS", money.getCurrencyCode());
        assertEquals(100L, money.getUnits());
        assertEquals(500_000_000, money.getNanos());
    }

    @Test
    void toProto_shouldAcceptTrailingZerosBeyondNanosScale() {
        Money money = MoneyAdapter.toProto("NDS", new BigDecimal("1.2300000000"));
        assertNotNull(money);
        assertEquals(1L, money.getUnits());
        assertEquals(230_000_000, money.getNanos());
    }

    @Test
    void toProto_shouldRejectMoreThanNineFractionalDigits() {
        assertThrows(IllegalArgumentException.class, () ->
            MoneyAdapter.toProto("NDS", new BigDecimal("0.0000000001"))
        );
    }

    @Test
    void fromProto_shouldConvertBackToBigDecimal() {
        Money money = Money.newBuilder()
            .setCurrencyCode("NDS")
            .setUnits(-1L)
            .setNanos(-250_000_000)
            .build();

        BigDecimal value = MoneyAdapter.fromProto(money);
        assertNotNull(value);
        assertEquals(new BigDecimal("-1.250000000"), value);
    }
}

