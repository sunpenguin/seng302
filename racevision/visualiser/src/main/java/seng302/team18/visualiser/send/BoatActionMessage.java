package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;

/**
 * Created by csl62 on 2/07/17.
 */
public class BoatActionMessage implements MessageBody{

    private boolean autopilot;
    private boolean sailsIn;
    private boolean sailsOut;
    private boolean tackGybe;
    private boolean upwind;
    private boolean downwind;

    @Override
    public int getType() {
        return 100;
    }

    public BoatActionMessage(boolean autopilot, boolean sailsIn, boolean sailsOut, boolean tackGybe, boolean upwind, boolean downwind) {
        this.autopilot = autopilot;
        this.sailsIn = sailsIn;
        this.sailsOut = sailsOut;
        this.tackGybe = tackGybe;
        this.upwind = upwind;
        this.downwind = downwind;
    }

    public boolean isAutopilot() {
        return autopilot;
    }

    public boolean isSailsIn() {
        return sailsIn;
    }

    public boolean isSailsOut() {
        return sailsOut;
    }

    public boolean isTackGybe() {
        return tackGybe;
    }

    public boolean isUpwind() {
        return upwind;
    }

    public boolean isDownwind() {
        return downwind;
    }
}
