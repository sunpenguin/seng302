package seng302.team18.model.updaters;

import seng302.team18.model.*;

/**
 * Updates the speed of participants over time
 */
public class SpeedUpdater implements Updater {


    private final double interval;
    private final double multiplier;
    private final double initialSpeed;
    private long totalTime = 0;
    private long timeSinceUpdate = 0;
    private double speed = 0;


    /**
     * Construct a new SpeedUpdater.
     *
     * speed is calculated every interval milliseconds to be
     * totalTimePassed (milliseconds) * multiplier + initalSpeed.
     *
     * @param interval time to wait between updates (milliseconds).
     * @param multiplier amount to increase speed by.
     * @param initialSpeed of the boats.
     */
    public SpeedUpdater(double interval, double multiplier, double initialSpeed) {
        this.interval = interval;
        this.multiplier = multiplier;
        this.initialSpeed = initialSpeed;
    }


    /**
     * Update the speed of the boat if time since last update exceeds interval (milliseconds).
     *
     * @param race to update
     * @param time to update by (in milliseconds)
     */
    @Override
    public void update(Race race, double time) {
        if (!RaceStatus.STARTED.equals(race.getStatus())) {
            return;
        }
        totalTime += time;
        timeSinceUpdate += time;
        if (timeSinceUpdate >= interval) {
            speed = totalTime * multiplier + initialSpeed;
            increaseSpeed(race);
            timeSinceUpdate = 0;
        }
    }


    /**
     * Increase speed of all boats in the race by applying a speed power up to each boat.
     *
     * @param race to apply speed power ups to it's participants.
     */
    private void increaseSpeed(Race race) {
        for (Boat boat : race.getStartingList()) {
            PowerUp powerUp = new SpeedPowerUp(speed);
            powerUp.setDuration(interval);
            boat.setPowerUp(powerUp);
            boat.activatePowerUp();
        }
    }

}
