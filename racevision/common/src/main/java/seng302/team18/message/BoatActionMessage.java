package seng302.team18.message;

import seng302.team18.message.MessageBody;

/**
 * MessageBody that contains information about a boats actions.
 */
public class BoatActionMessage implements MessageBody{

    private boolean autopilot = false;
    private boolean sailsIn = false;
    private boolean tackGybe = false;
    private boolean upwind = false;
    private boolean downwind = false;

    @Override
    public int getType() {
        return 100;
    }

    /**
     * No args constructor for the BoatActionMessage sets all values to false.
     */
    public BoatActionMessage() {}


    /**
     * Constructor for the BoatActionMessage.
     *
     * @param autopilot the autopilot property
     * @param sailsIn the sailsIn property
     * @param tackGybe the tackGybe property
     * @param upwind the upwind property
     * @param downwind the downwind property
     */
    public BoatActionMessage(boolean autopilot, boolean sailsIn, boolean tackGybe, boolean upwind, boolean downwind) {
        this.autopilot = autopilot;
        this.sailsIn = sailsIn;
        this.tackGybe = tackGybe;
        this.upwind = upwind;
        this.downwind = downwind;
    }


    public boolean isAutopilot() {
        return autopilot;
    }


    public void setAutopilot(boolean autopilot) {
        this.autopilot = autopilot;
    }


    public boolean isSailsIn() {
        return sailsIn;
    }


    public void setSailsIn(boolean sailsIn) {
        this.sailsIn = sailsIn;
    }


    public boolean isTackGybe() {
        return tackGybe;
    }


    public void setTackGybe(boolean tackGybe) {
        this.tackGybe = tackGybe;
    }


    public boolean isUpwind() {
        return upwind;
    }


    public void setUpwind(boolean upwind) {
        this.upwind = upwind;
    }


    public boolean isDownwind() {
        return downwind;
    }


    public void setDownwind(boolean downwind) {
        this.downwind = downwind;
    }
}
