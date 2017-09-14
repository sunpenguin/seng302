package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * Created by spe76 on 6/09/17.
 */
public class SharkPowerUp extends PowerUp {

    private BoatPowerUpUpdater updater = new BoatPowerUpUpdater();


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
