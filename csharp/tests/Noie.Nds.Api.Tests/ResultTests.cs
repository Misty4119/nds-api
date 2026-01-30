using FluentAssertions;
using Noie.Nds.Api.Result;
using Xunit;

namespace Noie.Nds.Api.Tests;

public class ResultTests
{
    [Fact]
    public void Success_ShouldCreateSuccessResult()
    {
        // Arrange & Act
        var result = NdsResult<int>.Success(42);
        
        // Assert
        result.IsSuccess.Should().BeTrue();
        result.IsFailure.Should().BeFalse();
        result.Data.Should().Be(42);
    }
    
    [Fact]
    public void Failure_ShouldCreateFailureResult()
    {
        // Arrange & Act
        var result = NdsResult<int>.Failure("ERROR_CODE", "Error message");
        
        // Assert
        result.IsSuccess.Should().BeFalse();
        result.IsFailure.Should().BeTrue();
        result.Error.Code.Should().Be("ERROR_CODE");
        result.Error.Message.Should().Be("Error message");
    }
    
    [Fact]
    public void Map_OnSuccess_ShouldTransformValue()
    {
        // Arrange
        var result = NdsResult<int>.Success(10);
        
        // Act
        var mapped = result.Map(x => x * 2);
        
        // Assert
        mapped.IsSuccess.Should().BeTrue();
        mapped.Data.Should().Be(20);
    }
    
    [Fact]
    public void Map_OnFailure_ShouldPreserveError()
    {
        // Arrange
        var result = NdsResult<int>.Failure("ERROR", "Something went wrong");
        
        // Act
        var mapped = result.Map(x => x * 2);
        
        // Assert
        mapped.IsFailure.Should().BeTrue();
        mapped.Error.Code.Should().Be("ERROR");
    }
    
    [Fact]
    public void FlatMap_ShouldChainResults()
    {
        // Arrange
        var result = NdsResult<int>.Success(10);
        
        // Act
        var chained = result.FlatMap(x => 
            x > 5 
                ? NdsResult<string>.Success($"Value is {x}") 
                : NdsResult<string>.Failure("TOO_SMALL", "Value too small"));
        
        // Assert
        chained.IsSuccess.Should().BeTrue();
        chained.Data.Should().Be("Value is 10");
    }
    
    [Fact]
    public void Match_ShouldExecuteCorrectBranch()
    {
        // Arrange
        var success = NdsResult<int>.Success(42);
        var failure = NdsResult<int>.Failure("ERROR", "Failed");
        
        // Act
        var successMessage = success.Match(
            onSuccess: x => $"Got {x}",
            onFailure: e => $"Error: {e.Code}");
        
        var failureMessage = failure.Match(
            onSuccess: x => $"Got {x}",
            onFailure: e => $"Error: {e.Code}");
        
        // Assert
        successMessage.Should().Be("Got 42");
        failureMessage.Should().Be("Error: ERROR");
    }
    
    [Fact]
    public void GetOrDefault_ShouldReturnDefaultOnFailure()
    {
        // Arrange
        var failure = NdsResult<int>.Failure("ERROR", "Failed");
        
        // Act
        var value = failure.GetOrDefault(99);
        
        // Assert
        value.Should().Be(99);
    }
    
    [Fact]
    public void ImplicitConversion_FromValue_ShouldCreateSuccess()
    {
        // Arrange & Act
        NdsResult<int> result = 42;
        
        // Assert
        result.IsSuccess.Should().BeTrue();
        result.Data.Should().Be(42);
    }
    
    [Fact]
    public void ImplicitConversion_FromError_ShouldCreateFailure()
    {
        // Arrange & Act
        NdsResult<int> result = NdsError.Of("ERROR", "Failed");
        
        // Assert
        result.IsFailure.Should().BeTrue();
        result.Error.Code.Should().Be("ERROR");
    }
}
