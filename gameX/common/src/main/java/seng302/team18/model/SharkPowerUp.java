package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * A PowerUp that can be fired at an opponent to stun them
 */
public class SharkPowerUp extends PowerUp {

    private BoatUpdater updater = new BoatUpdater();


    public SharkPowerUp() {
        super();
    }


    @Override
    public void update(Boat boat, double time) {
        super.update(boat, time);
        updater.update(boat, time);
    }

    @Override
    public PowerType getType() {
        return PowerType.SHARK;
    }
}
