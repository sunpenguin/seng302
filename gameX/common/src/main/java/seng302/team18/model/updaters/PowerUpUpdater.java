package seng302.team18.model.updaters;

import seng302.team18.model.*;

/**
 * Class to update boats on power ups
 */
public class PowerUpUpdater implements Updater {

    private final int powers;
    private final PickUp prototype;


    public PowerUpUpdater(PickUp pickUp, int numPowers) {
        prototype = pickUp;
        powers = numPowers;
    }


    /**
     * Updates all boats' power-ups
     *
     * @param race not null
     */
    @Override
    public void update(Race race, double time) {
        race.removeOldPickUps();
        if (race.getStatus().equals(RaceStatus.STARTED)) {
            addPowerUps(race);
        }
        shrinkPowerUps(race);

        for (Boat boat : race.getStartingList()) {
            powerUpStuff(race, boat);
        }
    }


    /**
     * Determines if a boat picked up a power up.
     *
     * @param boat not null
     */
    private void powerUpStuff(Race race, Boat boat) {
        for (PickUp pickUp : race.getPickUps()) {
            if (boat.hasCollided(pickUp.getBodyMass())) {
                final double duration = (pickUp.getTimeout() - System.currentTimeMillis());
                race.consumePowerUp(boat, pickUp);
                race.addPickUps(1, prototype, duration);
            }
        }
    }


    /**
     * Shrinks the power ups with reference to time left.
     *
     * @param race not null
     */
    private void shrinkPowerUps(Race race) {
        final double THIRTY_SECONDS_IN_MILLISECONDS = 30 * 1000;
        final double minRadius = 6;
        final double currentTime = System.currentTimeMillis();
        final double oldRadius = prototype.getRadius();
        for (PickUp pickUp : race.getPickUps()){
            double newRadius = (pickUp.getTimeout() - currentTime) / THIRTY_SECONDS_IN_MILLISECONDS * oldRadius;
            if (newRadius < minRadius) {
                newRadius = minRadius;
            }
            pickUp.setRadius(newRadius);
        }
    }


    /**
     * Adds new power ups to the race if there are some missing.
     *
     * @param race not null please.
     */
    private void addPowerUps(Race race) {
        final double THIRTY_SECONDS_IN_MILLISECONDS = 30 * 1000;
        final int numberOfPickUpsToAddToTheRace = powers - race.getPickUps().size();
        race.addPickUps(numberOfPickUpsToAddToTheRace, prototype, THIRTY_SECONDS_IN_MILLISECONDS);
    }
}
