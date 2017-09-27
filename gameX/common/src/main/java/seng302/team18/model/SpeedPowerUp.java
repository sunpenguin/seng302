package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * A PowerUP that increases the speed of a player when consumed
 */
public class SpeedPowerUp extends PowerUp {

    private BoatUpdater updater = new BoatUpdater();
    private double multiplier;

    public SpeedPowerUp(double multiplier) {
        super();
        this.multiplier = multiplier;
    }


    @Override
    public void update(Boat boat, double time) {
        super.update(boat, time);
        updater.update(boat, time * multiplier);
    }

    @Override
    public PowerType getType() {
        return PowerType.SPEED;
    }


    @Override
    public PowerUp clone() {
        return new SpeedPowerUp(multiplier);
    }


}
