import java.time.Duration;

public class MyHandler implements Handler{
    private final Client client;
    private final Duration timeout;

    public MyHandler(Client client, Duration timeout) {
        this.client = client;
        this.timeout = timeout;
    }

    @Override
    public ApplicationStatusResponse performOperation(String id) {
        long startTime = System.currentTimeMillis();

        int retriesCount = 0;
        while (true) {
            Response response1 = client.getApplicationStatus1(id);
            if (response1 instanceof Response.Success success) {
                return new ApplicationStatusResponse.Success(success.applicationId(), success.applicationStatus());
            } else if (response1 instanceof Response.RetryAfter retryAfter) {
                Duration delay = retryAfter.delay();
                if (System.currentTimeMillis() - startTime >= timeout.toMillis()) {
                    return new ApplicationStatusResponse.Failure(null, retriesCount);
                }
                waitForDelay(delay);
            } else {
                retriesCount++;
            }

            Response response2 = client.getApplicationStatus2(id);
            if (response2 instanceof Response.Success success) {
                return new ApplicationStatusResponse.Success(success.applicationId(), success.applicationStatus());
            } else if (response2 instanceof Response.RetryAfter retryAfter) {
                Duration delay = retryAfter.delay();
                if (System.currentTimeMillis() - startTime >= timeout.toMillis()) {
                    return new ApplicationStatusResponse.Failure(null, retriesCount);
                }
                waitForDelay(delay);
            } else {
                retriesCount++;
            }

            if (System.currentTimeMillis() - startTime >= timeout.toMillis()) {
                return new ApplicationStatusResponse.Failure(null, retriesCount);
            }
        }
    }

    private void waitForDelay(Duration delay) {
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException e) {
            System.out.println(String.format("Exception while processing {}", e));
        }
    }



}
