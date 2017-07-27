package seng302.team18.message;

/**
 * MessageBody that contains the source id allocated to a boat / player controller
 */
public class AcceptanceMessage implements MessageBody {

    private int sourceId;


    /**
     * Constructor for RegistrationMessage
     * @param sourceId the allocated sourceId for the players boat.
     */
    public AcceptanceMessage(int sourceId) {
        this.sourceId = sourceId;
    }


    @Override
    public int getType() {
        return AC35MessageType.ACCEPTANCE.getCode();
    }


    /**
     * Getter for the sourceId.
     * @return the sourceId
     */
    public int getSourceId() {
        return sourceId;
    }

}
