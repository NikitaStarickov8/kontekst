import java.util.List;

public class Event {

    private List<Address> recipients;
    private Payload payload;

    public List<Address> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Address> recipients) {
        this.recipients = recipients;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}