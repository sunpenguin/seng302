package seng302.team18.message;


/**
 * MessageBody that contains information about a boats actions.
 */
public class BoatActionMessage implements MessageBody {
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
        action = BoatActionStatus.AUTOPILOT.action();
    }


    public void setUpwind() {
        action = BoatActionStatus.UPWIND.action();
    }


    public void setDownwind() {
        action = BoatActionStatus.DOWNWIND.action();
    }


    public void setSailIn() {
        action = BoatActionStatus.SAIL_IN.action();
    }


    public void setSailOut() {
        action = BoatActionStatus.SAIL_OUT.action();
    }


    public void setTackGybe() {
        action = BoatActionStatus.TACK_GYBE.action();
    }


    public void setConsume() {
        action = BoatActionStatus.POWER_UP.action();
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
