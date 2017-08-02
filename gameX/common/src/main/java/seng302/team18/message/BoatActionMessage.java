package seng302.team18.message;

/**
 * MessageBody that contains information about a boats actions.
 */
public class BoatActionMessage implements MessageBody{
    private byte action;
    private int id = 420;

    @Override
    public int getType() {
        return 100;
    }


    /**
     * Constructor that takes in a boat id. Sets all values to false.
     *
     * @param id of the player boat.
     */
    public BoatActionMessage(int id) {
        this.id = id;
    }


    public void setAutoPilot() {
        action = 1;
    }


    public void setUpwind() {
        action = 5;
    }


    public void setDownwind() {
        action = 6;
    }


    public void setSailIn() {
        action = 2;
    }


    public void setSailOut() {
        action = 3;
    }


    public void setTackGybe() {
        action = 4;
    }


    /**
     * Return the action status of the message.
     *
     * @return a byte which represents the status of the BoatAction message.
     */
    public byte getAction() {
        return action;
    }


    /**
     * Getter for the boat id.
     *
     * @return boat id
     */
    public int getId() {
        return id;
    }
}
