package seng302.team18.message;

/**
 * Created by David-chan on 2/07/17.
 */
public class RequestMessage implements MessageBody{

    private int requestType;

    public RequestMessage(int requestType) {
        this.requestType = requestType;
    }

    @Override
    public int getType() {
        return AC35MessageType.REQUEST.getCode();
    }

    public int getRequestType() {
        return requestType;
    }
}
