using FluentAssertions;
using Nds.Ledger.V1;
using Noie.Nds.Api.Adapter;
using Xunit;

namespace Noie.Nds.Api.Tests;

public class MoneyAdapterTests
{
    [Fact]
    public void ToProto_ShouldConvertExactAmount()
    {
        var proto = MoneyAdapter.ToProto("NDS", 100.5m);

        proto.CurrencyCode.Should().Be("NDS");
        proto.Units.Should().Be(100L);
        proto.Nanos.Should().Be(500_000_000);
    }

    [Fact]
    public void ToProto_ShouldConvertNegativeAmount()
    {
        var proto = MoneyAdapter.ToProto("NDS", -1.25m);

        proto.Units.Should().Be(-1L);
        proto.Nanos.Should().Be(-250_000_000);
    }

    [Fact]
    public void ToProto_ShouldAcceptTrailingZerosBeyondNanosScale()
    {
        var proto = MoneyAdapter.ToProto("NDS", 1.2300000000m);

        proto.Units.Should().Be(1L);
        proto.Nanos.Should().Be(230_000_000);
    }

    [Fact]
    public void ToProto_ShouldRejectMoreThanNineFractionalDigits()
    {
        var act = () => MoneyAdapter.ToProto("NDS", 0.0000000001m);
        act.Should().Throw<ArgumentOutOfRangeException>();
    }

    [Fact]
    public void FromProto_ShouldConvertBackToDecimal()
    {
        var proto = new Money { CurrencyCode = "NDS", Units = -1L, Nanos = -250_000_000 };
        var value = MoneyAdapter.FromProto(proto);
        value.Should().Be(-1.25m);
    }
}

