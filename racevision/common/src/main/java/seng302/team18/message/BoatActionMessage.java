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
     * @param sailsIn the sailsIn property (accelerating is true, luffing is false)
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


    /**
     * Getter for the autopilot field
     *
     * @return The autopilot boolean
     */
    public boolean isAutopilot() {
        return autopilot;
    }

    /**
     * Setter for the sailsOut field
     *
     * @param autopilot The value to set the sailsOut filed to
     */
    public void setAutopilot(boolean autopilot) {
        this.autopilot = autopilot;
    }

    /**
     * Getter for the sailsIn field
     *
     * @return The sailsIn boolean
     */
    public boolean isSailsIn() {
        return sailsIn;
    }

    /**
     * Setter for the sailsOut field
     *
     * @param sailsIn The value to set the sailsOut filed to
     */
    public void setSailsIn(boolean sailsIn) {
        this.sailsIn = sailsIn;
    }


    public boolean isTackGybe() {
        return tackGybe;
    }

    /**
     * Setter for the sailsOut field
     *
     * @param tackGybe The value to set the sailsOut filed to
     */
    public void setTackGybe(boolean tackGybe) {
        this.tackGybe = tackGybe;
    }

    /**
     * Getter for the upwind field
     *
     * @return The upwind boolean
     */
    public boolean isUpwind() {
        return upwind;
    }

    /**
     * Setter for the sailsOut field
     *
     * @param upwind The value to set the sailsOut filed to
     */
    public void setUpwind(boolean upwind) {
        this.upwind = upwind;
    }

    /**
     * Getter for the downwind field
     *
     * @return The downwind boolean
     */
    public boolean isDownwind() {
        return downwind;
    }

    /**
     * Setter for the sailsOut field
     *
     * @param downwind The value to set the sailsOut filed to
     */
    public void setDownwind(boolean downwind) {
        this.downwind = downwind;
    }
}
