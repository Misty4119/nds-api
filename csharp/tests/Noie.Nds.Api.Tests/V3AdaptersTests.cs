using FluentAssertions;
using Nds.Common.V1;
using Noie.Nds.Api.Adapter;
using Noie.Nds.Api.Result;
using Xunit;

namespace Noie.Nds.Api.Tests;

public class V3AdaptersTests
{
    [Fact]
    public void RequestContextAdapter_ShouldCreateContext()
    {
        var ctx = V3RequestContextAdapter.Create("req-1", "idem-1");
        ctx.RequestId.Should().Be("req-1");
        ctx.IdempotencyKey.Should().Be("idem-1");
    }

    [Fact]
    public void ErrorStatusAdapter_ShouldRoundTrip()
    {
        var domain = NdsError.Of("SYSTEM_TIMEOUT", "timeout", new Dictionary<string, object> { ["k"] = "v" });
        var proto = V3ErrorStatusAdapter.ToProto(domain, ErrorCategory.Retryable, retryAfterSeconds: 3);

        proto.Code.Should().Be("SYSTEM_TIMEOUT");
        proto.Category.Should().Be(ErrorCategory.Retryable);
        proto.RetryAfterSeconds.Should().Be(3);
        proto.Details.Should().ContainKey("k");

        var back = V3ErrorStatusAdapter.FromProto(proto);
        back.Code.Should().Be("SYSTEM_TIMEOUT");
        back.Message.Should().Be("timeout");
        back.Details.Should().ContainKey("error_category");
    }
}

