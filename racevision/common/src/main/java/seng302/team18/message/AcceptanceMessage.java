package seng302.team18.message;

/**
 * MessageBody that contains the source id allocated to a boat / player controller
 */
public class AcceptanceMessage implements MessageBody {

    private int id;


    public AcceptanceMessage(int sourceId) {
        this.id = sourceId;
    }


    @Override
    public int getType() {
        return AC35MessageType.ACCEPTANCE.getCode();
    }


    public int getId() {
        return id;
    }


}
