import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MyHandler implements Handler {
    private final Client client;
    private final ExecutorService executorService;

    public MyHandler(Client client) {
        this.client = client;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public Duration timeout() {
        return Duration.ofMillis(100);
    }

    @Override
    public void performOperation() {
        while (true) {
            var event = client.readData();
            var payload = event.getPayload();

            event.getRecipients().forEach(address -> {
                executorService.submit(() -> {
                    while (true) {
                        var result = client.sendData(address, payload);
                        if (result == Result.ACCEPTED) break;
                        else {
                            try{
                                Thread.sleep(timeout().toMillis());
                            } catch (InterruptedException e){
                                System.out.println(String.format("Error while sending data to {} with cause {}",address, e.getCause()));
                            }
                        }
                    }
                });

            });
        }
    }
}
