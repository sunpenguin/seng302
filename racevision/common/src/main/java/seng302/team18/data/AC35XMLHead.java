package seng302.team18.data;

/**
 * Created by david on 4/10/17.
 */
public class AC35XMLHead  implements MessageHead {

    private AC35MessageType type;
    private int bodySize;

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