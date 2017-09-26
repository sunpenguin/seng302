package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * Created by sbe67 on 9/09/17.
 */
public class StunPowerUp extends PowerUp {

    public StunPowerUp() {
        super();
    }

    @Override
    public void update(Boat boat, double time) {
        super.update(boat, time);

    }

    @Override
    public boolean isTerminated() {
        return false;
    }


    @Override
    public PowerType getType() {
        return PowerType.STUN;
    }

    @Override
    public PowerUp clone() {
        return new StunPowerUp();
    }

}
