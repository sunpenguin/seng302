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


    /**
     * Getter for the boat id.
     *
     * @return boat id
     */
    public int getId() {
        return id;
    }
}
