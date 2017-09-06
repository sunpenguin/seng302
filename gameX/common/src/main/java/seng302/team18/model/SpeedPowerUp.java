package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * Created by dhl25 on 29/08/17.
 */
public class SpeedPowerUp extends PowerUp {

    private BoatUpdater updater = new BoatUpdater();

    public SpeedPowerUp() {
        super();
    }


    @Override
    public void update(Boat boat, double time) {
        super.update(boat, time);
        updater.update(boat, time * 3);
    }

    @Override
    public PowerType getType() {
        return PowerType.SPEED;
    }
}
