using FluentAssertions;
using Noie.Nds.Api.Identity;
using Xunit;

namespace Noie.Nds.Api.Tests;

public class IdentityTests
{
    [Fact]
    public void Of_ShouldCreateIdentity()
    {
        // Arrange & Act
        var identity = NdsIdentity.Of("test-id", IdentityType.Player);
        
        // Assert
        identity.Id.Should().Be("test-id");
        identity.Type.Should().Be(IdentityType.Player);
        identity.IsValid.Should().BeTrue();
    }
    
    [Fact]
    public void FromString_WithTypePrefix_ShouldParse()
    {
        // Arrange & Act
        var identity = NdsIdentity.FromString("SYSTEM:admin");
        
        // Assert
        identity.Id.Should().Be("admin");
        identity.Type.Should().Be(IdentityType.System);
    }
    
    [Fact]
    public void FromString_WithoutTypePrefix_ShouldDefaultToPlayer()
    {
        // Arrange & Act
        var identity = NdsIdentity.FromString("550e8400-e29b-41d4-a716-446655440000");
        
        // Assert
        identity.Id.Should().Be("550e8400-e29b-41d4-a716-446655440000");
        identity.Type.Should().Be(IdentityType.Player);
    }
    
    [Fact]
    public void WithMetadata_ShouldCreateNewInstance()
    {
        // Arrange
        var original = NdsIdentity.Of("test-id", IdentityType.Player);
        var metadata = new Dictionary<string, string> { ["key"] = "value" };
        
        // Act
        var updated = original.WithMetadata(metadata);
        
        // Assert
        updated.Should().NotBeSameAs(original);
        updated.Metadata.Should().ContainKey("key");
        original.Metadata.Should().BeEmpty();
    }
    
    [Theory]
    [InlineData("PLAYER", IdentityType.Player)]
    [InlineData("SYSTEM", IdentityType.System)]
    [InlineData("AI", IdentityType.Ai)]
    [InlineData("EXTERNAL", IdentityType.External)]
    [InlineData("UNKNOWN", IdentityType.Unknown)]
    [InlineData("invalid", IdentityType.Unknown)]
    public void IdentityType_FromString_ShouldParseCorrectly(string input, IdentityType expected)
    {
        // Act
        var result = IdentityTypeExtensions.FromString(input);
        
        // Assert
        result.Should().Be(expected);
    }
}
