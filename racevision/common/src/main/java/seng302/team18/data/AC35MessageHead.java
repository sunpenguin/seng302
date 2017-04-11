package seng302.team18.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageHead implements MessageHead {

    private AC35MessageType type;
    private int bodySize;

    public AC35MessageHead(AC35MessageType type, int bodySize) {
        this.type = type;
        this.bodySize = bodySize;
    }


    @Override
    public int getType() {
        return type.getCode();
    }

    @Override
    public int bodySize() {
        return bodySize;
    }
}
