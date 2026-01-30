using FluentAssertions;
using Nds.Common.V1;
using Nds.Event.V1;
using Nds.Identity.V1;
using Nds.Sync.V1;
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

    [Fact]
    public void ErrorStatusAdapter_ShouldPreserveRetryAfterSecondsPresence_WhenZero()
    {
        var domain = NdsError.Of("SYSTEM_UNAVAILABLE", "retry now", new Dictionary<string, object>());
        var proto = V3ErrorStatusAdapter.ToProto(domain, ErrorCategory.Retryable, retryAfterSeconds: 0);

        proto.HasRetryAfterSeconds.Should().BeTrue();
        proto.RetryAfterSeconds.Should().Be(0);

        var back = V3ErrorStatusAdapter.FromProto(proto);
        back.Details.Should().ContainKey("retry_after_seconds");
        back.Details["retry_after_seconds"].Should().Be(0);
    }

    [Fact]
    public void IdentityV1Adapter_ShouldRoundTripPersonaIdBytes()
    {
        var bytes = new byte[] { 10, 20, 30 };
        PersonaId id = V3IdentityV1Adapter.CreatePersonaId(bytes);
        V3IdentityV1Adapter.ToBytes(id).Should().Equal(bytes);
    }

    [Fact]
    public void EventV1Adapter_ShouldRoundTripCursorBytes()
    {
        var bytes = new byte[] { 1, 2, 3, 4 };
        Cursor cursor = V3EventV1Adapter.CreateCursor(bytes);
        V3EventV1Adapter.ToBytes(cursor).Should().Equal(bytes);
    }

    [Fact]
    public void SyncV1Adapter_ShouldRoundTripResumeTokenBytes()
    {
        var bytes = new byte[] { 9, 8, 7 };
        ResumeToken token = V3SyncV1Adapter.CreateResumeToken(bytes);
        V3SyncV1Adapter.ToBytes(token).Should().Equal(bytes);
    }
}

