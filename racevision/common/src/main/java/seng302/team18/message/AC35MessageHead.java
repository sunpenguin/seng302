package seng302.team18.message;

/**
 * MessageHead for the AC35 streaming protocol.
 */
public class AC35MessageHead implements MessageHead {

    private AC35MessageType type;
    private int bodySize;

    /**
     * Constructor for the AC35MessageHead.
     * @param type of the messages body.
     * @param bodySize size of the messages body.
     */
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
