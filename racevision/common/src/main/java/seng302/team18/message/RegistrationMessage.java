package seng302.team18.message;

/**
 * MessageBody that contains the source id.
 */
public class RegistrationMessage implements MessageBody {

    private int sourceId;

    /**
     * Constructor for RegistrationMessage
     * @param sourceId the allocated sourceId for the players boat.
     */
    public RegistrationMessage(int sourceId) {
        this.sourceId = sourceId;
    }


    @Override
    public int getType() {
        return 6;
    }


    /**
     * Getter for the sourceId.
     * @return the sourceId
     */
    public int getSourceId() {
        return sourceId;
    }
}
