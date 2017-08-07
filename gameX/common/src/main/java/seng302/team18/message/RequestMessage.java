package seng302.team18.message;

/**
 * Request message DTO for extension to controller protocol for client registration
 */
public class RequestMessage implements MessageBody {

    private RequestType action;

    public RequestMessage(RequestType action) {
        this.action = action;
    }


    @Override
    public int getType() {
        return AC35MessageType.REQUEST.getCode();
    }


    public RequestType getAction() {
        return action;
    }
}
