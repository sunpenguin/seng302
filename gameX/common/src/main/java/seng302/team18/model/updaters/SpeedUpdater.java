package seng302.team18.model.updaters;

import seng302.team18.model.*;

/**
 * Created by dhl25 on 7/09/17.
 */
public class SpeedUpdater implements Updater {


    private final double interval;
    private final double modifier;
    private double accumulatedTime = 0;
    private double accumulatedSpeed = 0;


    /**
     * Increases the speed of all boats in a race at the
     * given interval by the given modifier.
     *
     * Given an interval of 10 seconds and modifier of 3
     * after 10 seconds the boat will move 3 times faster
     * and after 20 seconds the boat will move 6 times faster.
     *
     * @param interval that speed increase is updated in milliseconds.
     * @param modifier to increase speed by.
     */
    public SpeedUpdater(double interval, double modifier) {
        this.interval = interval;
        this.modifier = modifier;
    }


    @Override
    public void update(Race race, double time) {
        if (!RaceStatus.STARTED.equals(race.getStatus())) {
            return;
        }
        accumulatedTime += time;
        if (accumulatedTime >= interval) {
            accumulatedSpeed += modifier;
            increaseSpeed(race, accumulatedSpeed);
            accumulatedTime = 0;
        }
    }


    /**
     * Increases the speed of all boats in a race by the given multiplier.
     * This is done using speed power ups.
     *
     * @param race to get boats from.
     * @param multiplier for speed.
     */
    private void increaseSpeed(Race race, double multiplier) {
        for (Boat boat : race.getStartingList()) {
            PowerUp powerUp = new SpeedPowerUp(multiplier);
            powerUp.setDuration(interval);
            boat.setPowerUp(powerUp);
            boat.activatePowerUp();
        }
    }

}
