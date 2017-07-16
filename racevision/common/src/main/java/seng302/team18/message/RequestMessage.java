package seng302.team18.message;

/**
 * Created by David-chan on 2/07/17.
 */
public class RequestMessage implements MessageBody{

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
