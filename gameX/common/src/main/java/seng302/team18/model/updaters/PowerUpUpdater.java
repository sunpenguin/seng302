package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.Arrays;

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
        for (Boat boat : race.getStartingList()) {
            powerUpStuff(boat);
        }
    }


    /**
     * Determines if a boat picked up a power up.
     *
     * @param boat
     */
    private void powerUpStuff(Boat boat) {

    }
}
