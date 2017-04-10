package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageHead implements MessageHead {

    private MessageType type;
    private int bodySize;

    public AC35MessageHead(MessageType type, int bodySize) {
        this.type = type;
        this.bodySize = bodySize;
    }


    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public int bodySize() {
        return bodySize;
    }
}
