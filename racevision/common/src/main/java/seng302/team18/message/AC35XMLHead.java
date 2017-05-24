package seng302.team18.message;

/**
 * MessageHead for the XML files sent by the AC35 streaming protocol.
 */
public class AC35XMLHead implements MessageHead {

    private AC35MessageType type;
    private int bodySize;

    /**
     * Constructor for the AC35XMLHead.
     *
     * @param type     of the message's body.
     * @param bodySize size of the message's body.
     */
    public AC35XMLHead(AC35MessageType type, int bodySize) {
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