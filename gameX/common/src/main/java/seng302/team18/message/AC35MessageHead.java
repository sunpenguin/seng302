package seng302.team18.message;

/**
 * MessageHead for the AC35 streaming protocol.
 */
public class AC35MessageHead implements MessageHead {

    private AC35MessageType type;
    private int bodySize;

    /**
     * Constructor for the AC35MessageHead.
     *
     * @param type     of the message body.
     * @param bodySize size of the message body.
     */
    public AC35MessageHead(AC35MessageType type, int bodySize) {
        this.type = type;
        this.bodySize = bodySize;
    }

    /**
     * Gets the code of the message type.
     *
     * @return the code of the message type.
     */
    @Override
    public int getType() {
        return type.getCode();
    }


    /**
     * Gets the size of the message body.
     *
     * @return the size of the message body.
     */
    @Override
    public int bodySize() {
        return bodySize;
    }
}
