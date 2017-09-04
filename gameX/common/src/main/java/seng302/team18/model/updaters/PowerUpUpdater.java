package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to update boats on power ups
 */
public class PowerUpUpdater implements Updater {

    /**
     * Updates all boats' power-ups
     *
     * @param race
     */
    @Override
    public void update(Race race) {
//        removeOldPickUps(race);

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
                race.consumePowerUp(boat, pickUp);
            }
        }
    }


    /**
     * Removes all expired power ups from the given race.
     *
     * @param race not null.
     */
    private void removeOldPickUps(Race race) {
        List<PickUp> pickUps = new ArrayList<>();
        for (PickUp pickUp : race.getPickUps()) {
            if (!pickUp.hasExpired()) {
                pickUps.add(pickUp);
            }
        }
        race.setPickUps(pickUps);
    }
}
