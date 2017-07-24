package seng302.team18.message;

/**
 * Request message DTO for extension to controller protocol for client registration
 */
public class RequestMessage implements MessageBody {

    private boolean isParticipating;

    public RequestMessage(boolean isParticipating) {
        this.isParticipating = isParticipating;
    }


    @Override
    public int getType() {
        return AC35MessageType.REQUEST.getCode();
    }


    public boolean isParticipating() {
        return isParticipating;
    }
}
