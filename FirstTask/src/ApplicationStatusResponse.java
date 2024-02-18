import java.time.Duration;

public sealed interface ApplicationStatusResponse permits ApplicationStatusResponse.Failure, ApplicationStatusResponse.Success {
    record Failure(Duration lastRequestTime, int retriesCount) implements ApplicationStatusResponse {}
    record Success(String id, String status) implements ApplicationStatusResponse {}
}